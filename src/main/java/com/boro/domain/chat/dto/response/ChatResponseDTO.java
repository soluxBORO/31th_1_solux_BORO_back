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
            String chatRoomName,
            String postName,
            String profileUrl,
            List<ChatMessageDetail> chatMessageList
    ){}

    @Builder
    public record ChatRoomPreview(
            Long chatRoomId,
            String chatName,
            String profileUrl,
            String lastMessageContent,
            LocalDateTime lastMessageAt,
            Integer unreadCount
    ){}
    
    // 채팅방 리스트 조회
    @Builder
    public record ChatRoomList(
        ChatRoomType chatRoomType,
        List<ChatRoomPreview> chatRoomList
    ){}

}
