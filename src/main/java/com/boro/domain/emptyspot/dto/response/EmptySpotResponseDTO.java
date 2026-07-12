package com.boro.domain.emptyspot.dto.response;

import com.boro.domain.post.entity.enums.PostStatus;
import lombok.Builder;

import java.time.LocalDateTime;

public record EmptySpotResponseDTO() {

    @Builder
    public record EmptySpotDetail(
            Long postId,
            String location,
            Integer floor,
            Integer seatNumber,
            Boolean hasPowerOutlet,
            Boolean hasWindowSeat,
            LocalDateTime expectedCheckoutTime
    ) {}

    @Builder
    public record EmptySpotSummary(
            Long emptySpotId,
            String location,
            Integer floor,
            Integer seatNumber,
            Boolean hasPowerOutlet,
            Boolean hasWindowSeat,
            LocalDateTime expectedCheckoutTime,
            LocalDateTime createdAt,
            String authorNickname
    ) {}

    @Builder
    public record EmptySpotInfo(
            Long emptySpotId,
            PostStatus status,
            String location,
            Integer floor,
            Integer seatNumber,
            Boolean hasPowerOutlet,
            Boolean hasWindowSeat,
            LocalDateTime expectedCheckoutTime,
            LocalDateTime createdAt,
            String authorNickname
    ) {}
}
