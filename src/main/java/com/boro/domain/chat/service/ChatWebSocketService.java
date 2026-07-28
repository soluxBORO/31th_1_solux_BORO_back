package com.boro.domain.chat.service;

import com.boro.domain.chat.dto.request.ChatRequestDTO;
import com.boro.domain.chat.dto.response.ChatResponseDTO;
import com.boro.domain.chat.service.command.ChatCommandService;
import com.boro.domain.chat.service.command.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatWebSocketService {

    private final ChatCommandService chatCommandService;
    private final RedisPublisher redisPublisher;

    public ChatResponseDTO.ChatMessage sendMessage(Long memberId, Long roomId, ChatRequestDTO.ChatMessage request){
        ChatResponseDTO.ChatMessage message = chatCommandService.saveMessage(memberId, roomId, request);
        redisPublisher.publishChatMessage(message);
        return message;
    }
}
