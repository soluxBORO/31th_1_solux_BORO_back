package com.boro.domain.chat.controller;

import com.boro.domain.chat.dto.request.ChatRequestDTO;
import com.boro.domain.chat.service.ChatWebSocketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
@Tag(name = "채팅 API")
public class ChatWebSocketController {
    private final ChatWebSocketService chatWebSocketService;
    private static final String AUTHENTICATED_MEMBER_ID = "AUTHENTICATED_MEMBER_ID";

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(
            @Payload ChatRequestDTO.ChatMessage request,
            @DestinationVariable Long roomId,
            SimpMessageHeaderAccessor accessor
    ){
        log.info("===== Controller sendMessage 진입 =====");
        Long memberId = Long.parseLong(accessor.getSessionAttributes().get(AUTHENTICATED_MEMBER_ID).toString());
        log.info("roomId: {}", roomId);
        chatWebSocketService.sendMessage(memberId, roomId, request);
    }
}
