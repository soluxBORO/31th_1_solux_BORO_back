package com.boro.domain.emptyspot.dto.response;

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
}
