package com.boro.domain.rentalrequest.dto.response;

import lombok.Builder;

import java.time.LocalDate;

public record RentalRequestResponseDTO() {

    public enum DisplayStatus {
        PENDING, RENTING, RETURNED, REJECTED
    }

    @Builder
    public record BorrowedItem(
            DisplayStatus requestStatus,
            LocalDate rentalStartTime,
            String lender,
            String title
    ) {}
}
