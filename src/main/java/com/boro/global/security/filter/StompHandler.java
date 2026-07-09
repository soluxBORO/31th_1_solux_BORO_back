package com.boro.global.security.filter;

import com.boro.global.error.code.status.GeneralErrorCode;
import com.boro.global.error.exception.handler.AuthException;
import com.boro.global.security.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.info("===== STOMP preSend =====");
        log.info("STOMP Command = {}", accessor.getCommand());
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            accessor.setLeaveMutable(true);
            log.info("CONNECT 요청 수신");
            String token = extractToken(accessor);
            log.info("Extract Token : {}", token.substring(0, 20) + "...");
            Long memberId = Long.parseLong(jwtProvider.getAuthentication(token).getName());
            accessor.getSessionAttributes().put("AUTHENTICATED_MEMBER_ID", memberId);
            return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
        } else if (StompCommand.SEND.equals(accessor.getCommand())) {
            log.info("===== SEND 요청 수신 =====");
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            log.info("===== SUBSCRIBE 요청 수신 =====");
        }
        return message;
    }

    private String extractToken(StompHeaderAccessor accessor) {
        String authHeader = accessor.getFirstNativeHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthException(GeneralErrorCode.UNAUTHORIZED);
        }

        return authHeader.substring(7);
    }
}
