package com.boro.domain.rentalrequest.converter;

import com.boro.domain.member.entity.Member;
import com.boro.domain.post.entity.Item;
import com.boro.domain.post.entity.Post;
import com.boro.domain.rentalrequest.dto.request.RentalRequestRequestDTO;
import com.boro.domain.rentalrequest.dto.response.RentalRequestResponseDTO;
import com.boro.domain.rentalrequest.entity.RentalRequest;
import com.boro.domain.rentalrequest.entity.Review;
import com.boro.domain.rentalrequest.entity.enums.RentalRequestStatus;

public class RentalRequestConverter {

    public static RentalRequestResponseDTO.BorrowedItem toBorrowedItem(RentalRequest rentalRequest) {
        Item item = rentalRequest.getPost().getItem();
        return RentalRequestResponseDTO.BorrowedItem.builder()
                .requestStatus(toDisplayStatus(rentalRequest))
                .rentalStartTime(item.getRentalStartTime())
                .lender(rentalRequest.getPost().getMember().getNickname())
                .title(item.getTitle())
                .build();
    }

    public static RentalRequestResponseDTO.LentItem toLentItem(RentalRequest rentalRequest) {
        Item item = rentalRequest.getPost().getItem();
        return RentalRequestResponseDTO.LentItem.builder()
                .requestStatus(toDisplayStatus(rentalRequest))
                .rentalStartTime(item.getRentalStartTime())
                .rentalEndTime(item.getRentalEndTime())
                .borrower(rentalRequest.getMember().getNickname())
                .title(item.getTitle())
                .build();
    }

    private static RentalRequestResponseDTO.DisplayStatus toDisplayStatus(RentalRequest rentalRequest) {
        if (rentalRequest.getRequestStatus() == RentalRequestStatus.REJECTED) {
            return RentalRequestResponseDTO.DisplayStatus.REJECTED;
        }
        if (rentalRequest.getRequestStatus() == RentalRequestStatus.PENDING) {
            return RentalRequestResponseDTO.DisplayStatus.PENDING;
        }
//        return rentalRequest.getProgressStatus() == RentalProgressStatus.RETURNED
//                ? RentalRequestResponseDTO.DisplayStatus.RETURNED
//                : RentalRequestResponseDTO.DisplayStatus.RENTING;
        return null;
    }

    public static RentalRequest toRentalRequest(Member member, Post post){
        return RentalRequest.builder()
                .member(member)
                .post(post)
                .build();
    }

    public static Review toReview(Member writer, Member receiver, RentalRequest rentalRequest, RentalRequestRequestDTO.Review request){
        return Review.builder()
                .reviewSentiment(request.reviewSentiment())
                .content(request.content())
                .receiver(receiver)
                .writer(writer)
                .rentalRequest(rentalRequest)
                .build();
    }

    public static RentalRequestResponseDTO.DecisionResult toDecisionResult(RentalRequest rentalRequest) {
        return RentalRequestResponseDTO.DecisionResult.builder()
                .rentalRequestStatus(rentalRequest.getRequestStatus())
                .borrowerReturned(rentalRequest.isBorrowerReturned())
                .ownerReturned(rentalRequest.isOwnerReturned())
                .build();
    }

    public static RentalRequestResponseDTO.CreatedRentalRequest toCreatedRentalRequest(RentalRequest rentalRequest) {
        return RentalRequestResponseDTO.CreatedRentalRequest.builder()
                .rentalRequestId(rentalRequest.getId())
                .requestStatus(rentalRequest.getRequestStatus())
                .borrowerReturned(rentalRequest.isBorrowerReturned())
                .ownerReturned(rentalRequest.isOwnerReturned())
                .memberId(rentalRequest.getMember().getId())
                .postId(rentalRequest.getPost().getId())
                .build();
    }

    public static RentalRequestResponseDTO.CreatedReview toCreatedReview(Review review){
        return RentalRequestResponseDTO.CreatedReview.builder()
                .reviewId(review.getId())
                .reviewSentiment(review.getReviewSentiment())
                .content(review.getContent())
                .writerId(review.getWriter().getId())
                .receiverId(review.getReceiver().getId())
                .rentalRequestId(review.getRentalRequest().getId())
                .build();
    }
}
