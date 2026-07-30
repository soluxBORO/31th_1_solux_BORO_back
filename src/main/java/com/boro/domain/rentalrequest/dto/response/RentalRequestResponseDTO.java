package com.boro.domain.rentalrequest.dto.response;

import com.boro.domain.post.entity.enums.PostCategory;
import com.boro.domain.post.entity.enums.RentalPriceUnit;
import com.boro.domain.rentalrequest.entity.enums.RentalRequestStatus;
import com.boro.domain.rentalrequest.entity.enums.ReviewSentiment;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RentalRequestResponseDTO() {

    @Builder
    public record RentalRequestPreview(
            Long rentalRequestId,
            Long postId,
            String imageUrl,
            RentalRequestStatus rentalRequestStatus,
            PostCategory postCategory,
            String ownerNickname,
            LocalDateTime createdAt,

            ItemDetail itemDetail,
            SeatDetail seatDetail
    ){}

    @Builder
    public record ItemDetail(
            String title,
            LocalDate rentalStartTime,
            LocalDate rentalEndTime,
            Integer rentalPrice,
            RentalPriceUnit rentalPriceUnit
    ) {
    }

    @Builder
    public record SeatDetail(
            String location,
            Integer floor,
            Integer seatNumber,
            LocalDateTime expectedCheckoutTime,
            Boolean hasPowerOutlet,
            Boolean hasWindowSeat
    ) {
    }

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
