package com.boro.global.security.filter;

import com.boro.domain.chat.service.command.ChatRoomViewerService;
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
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;
    private final ChatRoomViewerService chatRoomViewerService;
    private static final String CHAT_DESTINATION_PREFIX = "/sub/chat/";
    private static final String SESSION_ROOM_ID = "SUBSCRIBED_ROOM_ID";
    private static final String AUTHENTICATED_MEMBER_ID = "AUTHENTICATED_MEMBER_ID";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        handleMessage(accessor.getCommand(), accessor);
        return message;
    }

    private void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor) {
        log.info("===== STOMP preSend =====");
        log.info("STOMP Command = {}", accessor.getCommand());
        switch (stompCommand) {
            case CONNECT:
                log.info("===== CONNECT 요청 수신 =====");
                String token = extractToken(accessor);
                Authentication authentication = jwtProvider.getAuthentication(token);
                Long memberId = Long.parseLong(authentication.getName());
                accessor.setUser(authentication);
                accessor.getSessionAttributes().put(AUTHENTICATED_MEMBER_ID, memberId);
                accessor.setLeaveMutable(true);
                log.info(
                        "STOMP 인증 완료: memberId={}, principal={}",
                        memberId,
                        accessor.getUser().getName()
                );
                break;
            case SUBSCRIBE:
                log.info("===== SUBSCRIBE 요청 수신 =====");
                handleSubscribe(accessor);
                break;
            case UNSUBSCRIBE:
                log.info("===== UNSUBSCRIBE 요청 수신 =====");
                // lastMessage 추가
                handleUnsubscribe(accessor);
                break;
            case DISCONNECT:
                log.info("===== DISCONNECT 요청 수신 =====");
                handleDisconnect(accessor);
                break;
            case SEND:
                log.info("===== SEND 요청 수신 =====");
                break;
        }
    }

    private void handleSubscribe(StompHeaderAccessor accessor){
        String destination = accessor.getDestination();
        if (destination == null || !destination.startsWith(CHAT_DESTINATION_PREFIX)) {
            return;
        }

        Long roomId = Long.valueOf(destination.substring(CHAT_DESTINATION_PREFIX.length()));

        Map<String, Object> sessionAttributes =
                accessor.getSessionAttributes();

        if (sessionAttributes == null) {
            throw new IllegalStateException("웹소켓 세션 정보가 없습니다.");
        }

        Long memberId = Long.parseLong(sessionAttributes.get(AUTHENTICATED_MEMBER_ID).toString());
        if (memberId == null) {
            throw new IllegalStateException("웹소켓 인증 회원 정보가 없습니다.");
        }
        chatRoomViewerService.enterRoom(roomId, memberId);
        log.info("entered: memberId, roomId: {}, {}", memberId, roomId);
        accessor.getSessionAttributes().put(SESSION_ROOM_ID, roomId);
    }

    private void handleUnsubscribe(StompHeaderAccessor accessor) {
        removeViewer(accessor);
    }

    private void handleDisconnect(StompHeaderAccessor accessor) {
        removeViewer(accessor);
    }

    private void removeViewer(StompHeaderAccessor accessor){
        log.info("== removeViewer ==");
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (sessionAttributes == null) return;

        Long roomId = (Long) sessionAttributes.get(SESSION_ROOM_ID);
        Long memberId = (Long) sessionAttributes.get(AUTHENTICATED_MEMBER_ID);

        if (roomId == null || memberId == null) return;

        chatRoomViewerService.leaveRoom(roomId, memberId);
        log.info("leaved: memberId, roomId: {}, {}", memberId, roomId);
        sessionAttributes.remove(SESSION_ROOM_ID);
    }

    private String extractToken(StompHeaderAccessor accessor) {
        String authHeader = accessor.getFirstNativeHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthException(GeneralErrorCode.UNAUTHORIZED);
        }

        return authHeader.substring(7);
    }
}
