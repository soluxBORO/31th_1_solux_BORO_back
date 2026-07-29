package com.boro.domain.chat.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatRoomPreview(
        Long chatRoomId,
        String chatName,
        String profileUrl,
        String lastMessageContent,
        LocalDateTime lastMessageAt,
        Long unreadCount,
        String postTitle,
        String location,
        Integer floor,
        Integer seatNumber,
        Boolean hasPowerOutlet,
        Boolean hasWindowSeat,
        LocalDateTime expectedCheckoutTime
) {
}
