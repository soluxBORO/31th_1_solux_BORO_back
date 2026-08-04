package com.boro.domain.chat.service.command;

import com.boro.domain.chat.dto.response.ChatResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ObjectMapper objectMapper;

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
            log.info("===== Redis Subscriber sendMessage 진입 =====");
            ChatResponseDTO.ChatRoomUpdate chatRoomUpdate = objectMapper.convertValue(payload, ChatResponseDTO.ChatRoomUpdate.class);

            messagingTemplate.convertAndSendToUser(
                    chatRoomUpdate.memberId().toString(), "/queue/unread", chatRoomUpdate
            );
        } catch (Exception e){
            log.error("Exception {}", e);
        }
    }
}
