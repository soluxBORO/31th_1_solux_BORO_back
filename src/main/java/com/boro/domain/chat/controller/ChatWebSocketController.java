package com.boro.domain.chat.controller;

import com.boro.domain.chat.dto.request.ChatRequestDTO;
import com.boro.domain.chat.service.ChatWebSocketService;
import com.boro.global.security.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Controller
@Tag(name = "채팅 API")
public class ChatWebSocketController {

    private final ChatWebSocketService chatWebSocketService;

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(
            ChatRequestDTO.ChatMessage request,
            @DestinationVariable Long roomId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        chatWebSocketService.sendMessage(customUserDetails.getMemberId(), roomId, request);
    }
}
