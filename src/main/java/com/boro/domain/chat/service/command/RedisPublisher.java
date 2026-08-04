package com.boro.domain.chat.service.command;

import com.boro.domain.chat.dto.response.ChatResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CHAT_MESSAGE_TOPIC_PREFIX = "chat.room.";
    private static final String CHAT_ROOM_UPDATE_TOPIC_PREFIX = "chat-room.update.";

    /**
     *  해당 Topic을 구독하는 모든 구독자에게 message가 발행
     *  채팅방 실시간 메시지 발행
     */
    public void publishChatMessage(ChatResponseDTO.ChatMessage message) {
        Long subscriberCount = redisTemplate.convertAndSend(CHAT_MESSAGE_TOPIC_PREFIX + message.roomId(), message);
        log.info(
                "Redis publish topic={}, subscriberCount={}",
                CHAT_MESSAGE_TOPIC_PREFIX + message.roomId(),
                subscriberCount
        );
    }

    /**
     * 사용자별 채팅방 목록 업데이트 발행
     */
    public void publishChatRoomUpdate(ChatResponseDTO.ChatRoomUpdate request){
        redisTemplate.convertAndSend(CHAT_ROOM_UPDATE_TOPIC_PREFIX+ request.memberId(), request);
    }
}
