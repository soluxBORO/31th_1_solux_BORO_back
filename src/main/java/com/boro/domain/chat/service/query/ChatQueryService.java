package com.boro.domain.chat.service.query;

import com.boro.domain.chat.converter.ChatConverter;
import com.boro.domain.chat.dto.response.ChatResponseDTO;
import com.boro.domain.chat.dto.response.ChatRoomPreview;
import com.boro.domain.chat.entity.ChatMember;
import com.boro.domain.chat.entity.ChatMessage;
import com.boro.domain.chat.entity.ChatRoom;
import com.boro.domain.chat.entity.enums.ChatRoomType;
import com.boro.domain.chat.repository.ChatMessageRepository;
import com.boro.domain.chat.repository.ChatRoomRepository;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.repository.MemberRepository;
import com.boro.global.error.code.status.ChatErrorCode;
import com.boro.global.error.code.status.MemberErrorCode;
import com.boro.global.error.exception.handler.ChatException;
import com.boro.global.error.exception.handler.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatQueryService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    // 채팅방 리스트 조회
    public ChatResponseDTO.ChatRoomList getChatRoomList(Long memberId, ChatRoomType chatRoomType){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        List<ChatRoomPreview> chatRoomList = null;
        if (chatRoomType == ChatRoomType.ITEM){
            chatRoomList = chatRoomRepository.findItemChatRoomList(memberId);
        } else if (chatRoomType == ChatRoomType.EMPTY_SPOT){
            chatRoomList = chatRoomRepository.findEmptySpotChatRoomList(memberId);
        }

        return ChatConverter.toChatRoomPreviewList(chatRoomType, chatRoomList);
    }

    // 채팅방 상세 조회
    public ChatResponseDTO.ChatMessageList getChatRoomDetail(Long memberId, Long chatRoomId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));
        Member opponent = chatRoom.getChatMemberList().stream()
                .map(ChatMember::getMember)
                .filter(m -> !m.getId().equals(memberId))
                .findFirst()
                .orElseThrow();
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllByChatRoomOrderByCreatedAtDesc(chatRoom);
        return ChatConverter.toChatMessageList(chatRoom, chatMessageList, opponent);
    }

    public long countUnreadMessages(Long roomId, Long memberId){
        return chatMessageRepository.countUnreadMessages(roomId, memberId);
    }
}
