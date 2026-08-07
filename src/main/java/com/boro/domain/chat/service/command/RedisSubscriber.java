package com.boro.domain.chat.service.command;

import com.boro.domain.chat.dto.response.ChatResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ObjectMapper objectMapper;
    private final SimpUserRegistry simpUserRegistry;

    /**
 * Redis에서 메시지가 발행(publish)되면
 * 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리
 *
 */
    public void sendMessage(Object payload) {
        try {
            log.info("===== Redis Subscriber sendMessage 진입 =====");
            ChatResponseDTO.ChatMessage chatMessage =
                    objectMapper.convertValue(payload, ChatResponseDTO.ChatMessage.class);

            messagingTemplate.convertAndSend(
                    "/sub/chat/" + chatMessage.roomId(), chatMessage
            );
        } catch (Exception e){
            log.error("Exception {}", e);
        }
    }

    public void handleChatRoomUpdate(Object payload){
        try {
            log.info("===== Redis Subscriber handleChatRoomUpdate 진입 =====");
            log.info("payload {}, {}",payload, payload.getClass());
            ChatResponseDTO.ChatRoomUpdate chatRoomUpdate = objectMapper.convertValue(payload, ChatResponseDTO.ChatRoomUpdate.class);
            String username = chatRoomUpdate.memberId().toString();
            SimpUser user = simpUserRegistry.getUser(username);

            if (user == null) {
                log.warn(
                        "WebSocket 접속 사용자 없음: username={}",
                        username
                );
            } else {
                log.info(
                        "WebSocket 사용자 확인: name={}, sessions={}",
                        user.getName(),
                        user.getSessions().size()
                );
                user.getSessions().forEach(session -> {
                    log.info("WebSocket sessionId={}, 구독 개수={}", session.getId(), session.getSubscriptions().size());
                });
                log.info("등록 사용자=[{}]", user.getName());
                log.info("전송 대상=[{}]", username);
                log.info("이름 일치={}", user.getName().equals(username));
            }

            messagingTemplate.convertAndSendToUser(
                    chatRoomUpdate.memberId().toString(), "/queue/unread", chatRoomUpdate
            );
            log.info("chatRoomUpdate {}",chatRoomUpdate);
        } catch (Exception e){
            log.error("Exception {}", e);
        }
    }
}
