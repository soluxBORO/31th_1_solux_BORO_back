package com.boro.domain.rentalrequest.dto.response;

import com.boro.domain.rentalrequest.entity.enums.RentalRequestStatus;
import com.boro.domain.rentalrequest.entity.enums.ReviewSentiment;
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

    @Builder
    public record LentItem(
            DisplayStatus requestStatus,
            LocalDate rentalStartTime,
            LocalDate rentalEndTime,
            String borrower,
            String title
    ) {}

    @Builder
    public record DecisionResult(
            RentalRequestStatus rentalRequestStatus,
            boolean borrowerReturned,
            boolean ownerReturned
    ){}

    @Builder
    public record CreatedRentalRequest(
            Long rentalRequestId,
            RentalRequestStatus requestStatus,
            boolean borrowerReturned,
            boolean ownerReturned,
            Long memberId,
            Long postId
    ){
    }

    @Builder
    public record CreatedReview(
            Long reviewId,
            ReviewSentiment reviewSentiment,
            String content,
            Long writerId,
            Long receiverId,
            Long rentalRequestId
    ){}
}
