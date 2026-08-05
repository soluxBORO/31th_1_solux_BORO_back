package com.boro.domain.chat.dto.response;

import com.boro.domain.chat.entity.enums.ChatMessageType;
import com.boro.domain.chat.entity.enums.ChatRoomType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class ChatResponseDTO {

    @Builder
    public record ChatMessage(
            Long roomId,
            ChatMessageType chatMessageType,
            Long memberId,
            String content,
            List<String> imageUrls,
            LocalDateTime createdAt
    ){}

    @Builder
    public record ChatMessageDetail(
            ChatMessageType chatMessageType,
            Long memberId,
            String content,
            List<String> imageUrls,
            LocalDateTime createdAt
    ){}

    @Builder
    public record ChatMessageList(
            Long chatRoomId,
            String chatRoomName,
            String postName,
            String profileUrl,
            List<ChatMessageDetail> chatMessageList
    ){}

    // 채팅방 리스트 조회
    @Builder
    public record ChatRoomList(
        ChatRoomType chatRoomType,
        List<com.boro.domain.chat.dto.response.ChatRoomPreview> chatRoomList
    ){}

    @Builder
    public record ChatRoomUpdate(
            Long memberId,
            Long chatRoomId,
            String lastMessageContent,
            LocalDateTime lastMessageAt,
            Long unreadCount
    ){}

    @Builder
    public record ChatMessageSentEvent(
            Long chatRoomId,
            Long messageId,
            Long senderId,
            String content,
            LocalDateTime createdAt
    ){}
}
