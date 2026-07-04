package com.boro.domain.chat.service;

import com.boro.domain.chat.dto.request.ChatRequestDTO;
import com.boro.domain.chat.dto.response.ChatResponseDTO;
import com.boro.domain.chat.service.command.ChatCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatCommandService chatCommandService;

    public ChatResponseDTO.ChatMessage sendMessage(Long memberId, Long roomId, ChatRequestDTO.ChatMessage request){
        ChatResponseDTO.ChatMessage response = chatCommandService.saveMessage(memberId, request);

        messagingTemplate.convertAndSend(
                "/sub/chat" + response.roomId(),
                response
        );
        return response;
    }
}
