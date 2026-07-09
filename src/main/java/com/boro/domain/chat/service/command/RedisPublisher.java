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

    /**
     *  해당 Topic을 구독하는 모든 구독자에게 message가 발행
     */
    public void publish(ChatResponseDTO.ChatMessage message) {
        redisTemplate.convertAndSend("chat.room." + message.roomId(), message);
    }
}
