package com.boro.domain.chat.service.command;

import com.boro.domain.chat.converter.ChatConverter;
import com.boro.domain.chat.dto.response.ChatResponseDTO;
import com.boro.domain.chat.entity.ChatMember;
import com.boro.domain.chat.entity.ChatMessage;
import com.boro.domain.chat.repository.ChatMemberRepository;
import com.boro.domain.chat.repository.ChatMessageRepository;
import com.boro.domain.chat.service.query.ChatQueryService;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.repository.MemberRepository;
import com.boro.global.error.code.status.ChatErrorCode;
import com.boro.global.error.code.status.MemberErrorCode;
import com.boro.global.error.exception.handler.ChatException;
import com.boro.global.error.exception.handler.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageEventListener {

    private final ChatQueryService chatQueryService;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomViewerService chatRoomViewerService;
    private final RedisPublisher redisPublisher;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChatMessageSent(ChatResponseDTO.ChatMessageSentEvent request){
        Member member = memberRepository.findById(request.senderId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        ChatMember opponent = chatMemberRepository.findOpponentId(request.chatRoomId(), member.getId())
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_OPPONENT_NOT_FOUND));
        ChatMessage chatMessage = chatMessageRepository.findById(request.messageId())
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_MESSAGE_NOT_FOUND));
        Long opponentId = opponent.getMember().getId();
        boolean opponentIsViewing = chatRoomViewerService.isViewingRoom(request.chatRoomId(), opponentId);
        long unreadCount = 0L;

        if (opponentIsViewing){
            log.info("상대방 접속 여부: opponentIsViewing={}", opponentIsViewing);
            log.info("opponentChatMember={}", opponent.getMember().getId());
            opponent.updateLastReadMessage(chatMessage);
        } else {
            unreadCount = chatQueryService.countUnreadMessages(request.chatRoomId(), opponentId);
        }
        ChatResponseDTO.ChatRoomUpdate chatRoomUpdate = ChatConverter.toChatRoomUpdate(opponentId, request.chatRoomId(), request.content(), request.createdAt(), unreadCount, opponentIsViewing);
        redisPublisher.publishChatRoomUpdate(chatRoomUpdate);
    }
}
