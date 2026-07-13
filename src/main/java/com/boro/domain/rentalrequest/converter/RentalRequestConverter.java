package com.boro.domain.rentalrequest.converter;

import com.boro.domain.member.entity.Member;
import com.boro.domain.post.entity.EmptySpot;
import com.boro.domain.post.entity.Item;
import com.boro.domain.post.entity.ItemImage;
import com.boro.domain.post.entity.Post;
import com.boro.domain.post.entity.enums.PostCategory;
import com.boro.domain.rentalrequest.dto.request.RentalRequestRequestDTO;
import com.boro.domain.rentalrequest.dto.response.RentalRequestResponseDTO;
import com.boro.domain.rentalrequest.entity.RentalRequest;
import com.boro.domain.rentalrequest.entity.Review;

import java.util.Objects;

public class RentalRequestConverter {

    public static RentalRequestResponseDTO.RentalRequestPreview toRentalRequestPreview(RentalRequest rentalRequest) {
        Post post = rentalRequest.getPost();

        RentalRequestResponseDTO.ItemDetail itemDetail = null;
        RentalRequestResponseDTO.SeatDetail seatDetail = null;
        String imageUrl = null;

        if (Objects.requireNonNull(post.getPostCategory()) == PostCategory.EMPTY_SPOTS) {
            Item item = post.getItem();
            itemDetail = toItemDetail(item);
            imageUrl = item.getItemImages().stream()
                    .findFirst()
                    .map(ItemImage::getImageUrl)
                    .orElse(null);
        } else {
            EmptySpot emptySpot = post.getEmptySpot();
            seatDetail = toSeatDetail(emptySpot);
        }

        return RentalRequestResponseDTO.RentalRequestPreview.builder()
                .rentalRequestId(rentalRequest.getId())
                .postId(post.getId())
                .imageUrl(imageUrl)
                .rentalRequestStatus(rentalRequest.getRequestStatus())
                .postCategory(post.getPostCategory())
                .ownerNickname(post.getMember().getNickname())
                .createdAt(rentalRequest.getCreatedAt())
                .build();
    }

    public static RentalRequestResponseDTO.ItemDetail toItemDetail(Item item){
        return RentalRequestResponseDTO.ItemDetail.builder()
                .title(item.getTitle())
                .rentalStartTime(item.getRentalStartTime())
                .rentalEndTime(item.getRentalEndTime())
                .rentalPrice(item.getRentalPrice())
                .rentalPriceUnit(item.getRentalPriceUnit())
                .build();
    }

    public static RentalRequestResponseDTO.SeatDetail toSeatDetail(EmptySpot emptySpot){
        return RentalRequestResponseDTO.SeatDetail.builder()
                .location(emptySpot.getLocation())
                .floor(emptySpot.getFloor())
                .hasPowerOutlet(emptySpot.getHasPowerOutlet())
                .hasWindowSeat(emptySpot.getHasWindowSeat())
                .build();
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
