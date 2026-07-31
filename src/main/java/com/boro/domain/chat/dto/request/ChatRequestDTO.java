package com.boro.domain.chat.dto.request;

import com.boro.domain.chat.entity.enums.ChatMessageType;
import com.boro.domain.chat.entity.enums.ChatRoomType;

import java.util.List;

public class ChatRequestDTO {

    public record ChatMessage(
            ChatMessageType chatMessageType,
            String content,
            List<String> imageUrls
    ){}

    public record ChatRoomTest(
            Long ownerId,
            Long postId,
            String chatRoomName,
            ChatRoomType chatRoomType
    ){}
}
