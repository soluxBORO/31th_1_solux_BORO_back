package com.boro.domain.member.converter;

import com.boro.domain.member.dto.response.MemberResponseDTO;
import com.boro.domain.member.entity.Asset;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.entity.MemberAsset;
import com.boro.domain.member.entity.PointHistory;
import com.boro.domain.post.entity.*;
import com.boro.domain.post.entity.enums.PostCategory;
import com.boro.domain.rentalrequest.entity.RentalRequest;
import com.boro.domain.rentalrequest.entity.Review;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class MemberConverter {

    public static MemberResponseDTO.MemberInfo toMemberInfo(Member member){
        return MemberResponseDTO.MemberInfo.builder()
                .profileUrl(member.getProfileUrl())
                .email(member.getEmail())
                .studentNumber(member.getStudentNumber())
                .name(member.getName())
                .nickname(member.getNickname())
                .point(member.getPoint())
                .build();
    }

    public static PointHistory toPointHistory(String pointName,int point){
        return PointHistory.builder()
                .pointName(pointName)
                .point(point)
                .build();
    }

    public static MemberResponseDTO.PointHistory toPointHistoryDTO(PointHistory pointHistory){
        return MemberResponseDTO.PointHistory.builder()
                .pointDescription(pointHistory.getPointName())
                .point(pointHistory.getPoint())
                .createdAt(pointHistory.getCreatedAt().toLocalDate())
                .build();
    }

    public static MemberResponseDTO.Review toReview(
            Integer likeCnt, Integer dislikeCnt, List<Review> reviewList, Member member
    ){
        List<MemberResponseDTO.ReviewDetail> list = reviewList.stream()
                .map(review -> MemberConverter.toReviewDetail(member, review))
                .toList();

        return MemberResponseDTO.Review.builder()
                .likeCount(likeCnt)
                .dislikeCount(dislikeCnt)
                .reviewDetailList(list)
                .build();
    }

    public static MemberResponseDTO.ReviewDetail toReviewDetail(Member member, Review review){
        Post post = review.getRentalRequest().getPost();
        String postTitle = null;
        if (post.getPostCategory()== PostCategory.EMPTY_SPOTS){
            postTitle = post.getEmptySpot().getLocation();
        } else {
            postTitle = post.getItem().getTitle();
        }

        return MemberResponseDTO.ReviewDetail.builder()
                .memberId(member.getId())
                .memberNickname(member.getNickname())
                .postTitle(postTitle)
                .createdAt(review.getCreatedAt().toLocalDate())
                .content(review.getContent())
                .build();
    }

    public static MemberResponseDTO.MemberAsset toMemberAsset(MemberAsset memberAsset){
        Asset asset = memberAsset.getAsset();
        return MemberResponseDTO.MemberAsset.builder()
                .itemId(asset.getId())
                .itemName(asset.getName())
                .itemCategory(asset.getAssetCategory())
                .equipped(memberAsset.isEquipped())
                .build();
    }

    public static MemberResponseDTO.MemberLikePost toMemberLikePost(PostLike postLike){
        Post post = postLike.getPost();
        Item item = post.getItem();
        return MemberResponseDTO.MemberLikePost.builder()
                .postId(post.getId())
                .postImageUrl(item.getItemImages().stream().findFirst() .map(ItemImage::getImageUrl).orElse(null))
                .postCategory(post.getPostCategory())
                .postStatus(post.getStatus())
                .postTitle(item.getTitle())
                .postDescription(item.getDescription())
                .requestCreatedAt(post.getCreatedAt().toLocalDate())
                .profileImageUrl(post.getMember().getProfileUrl())
                .postMemberNickname(post.getMember().getNickname())
                .price(item.getRentalPrice())
                .priceUnit(item.getRentalPriceUnit())
                .likeCount(post.getPostLikeList().size())
                .build();
    }

    public static MemberResponseDTO.MyPost toMyItemPost(Post post){
        Item item = post.getItem();
        return MemberResponseDTO.MyPost.builder()
                .postId(post.getId())
                .postStatus(post.getStatus())
                .postCategory(post.getPostCategory())
                .price(item.getRentalPrice())
                .priceUnit(item.getRentalPriceUnit())
                .postTitle(item.getTitle())
                .postDescription(item.getDescription())
                .requestCreatedAt(item.getRentalStartTime())
                .build();
    }

    public static MemberResponseDTO.MyPost toMyEmptySpotPost(Post post){

        EmptySpot emptySpot = post.getEmptySpot();
        long leftMinutes = Math.max(0, Duration.between(
                LocalDateTime.now(), emptySpot.getExpectedCheckoutTime()).toMinutes()
        );
        return MemberResponseDTO.MyPost.builder()
                .postId(post.getId())
                .postStatus(post.getStatus())
                .postCategory(post.getPostCategory())
                .location(emptySpot.getLocation())
                .floor(emptySpot.getFloor())
                .seatNumber(emptySpot.getSeatNumber())
                .requestCreatedAt(emptySpot.getExpectedCheckoutTime().toLocalDate())
                .leftMinutes(leftMinutes)
                .build();
    }

    public static List<MemberResponseDTO.MyRentalHistory> getMyRentalHistory(List<RentalRequest> rentalRequestList, Member member){
        return rentalRequestList.stream()
                .map(rentalRequest -> {
                    if (rentalRequest.getPost().getPostCategory() == PostCategory.EMPTY_SPOTS) {
                        return toRentalEmptySpotHistory(rentalRequest, member);
                    } else {
                        return toRentalItemHistory(rentalRequest, member);
                    }
                }).toList();
    }

    public static MemberResponseDTO.MyRentalHistory toRentalItemHistory(RentalRequest rentalRequest, Member member){
        Post post = rentalRequest.getPost();
        Item item = post.getItem();
        String opponentNickname = getOpponentNickname(member, post, rentalRequest);

        return MemberResponseDTO.MyRentalHistory.builder()
                .rentalRequestId(rentalRequest.getId())
                .postId(post.getId())
                .postStatus(post.getStatus())
                .postCategory(post.getPostCategory())
                .price(item.getRentalPrice())
                .priceUnit(item.getRentalPriceUnit())
                .postTitle(item.getTitle())
                .postMemberNickname(member.getNickname())
                .postDescription(item.getDescription())
                .rentalStartTime(item.getRentalStartTime())
                .rentalEndTime(item.getRentalEndTime())
                .opponentNickname(opponentNickname)
                .build();
    }

    public static MemberResponseDTO.MyRentalHistory toRentalEmptySpotHistory(RentalRequest rentalRequest, Member member){
        Post post = rentalRequest.getPost();
        EmptySpot emptySpot = post.getEmptySpot();
        String opponentNickname = getOpponentNickname(member, post, rentalRequest);

        return MemberResponseDTO.MyRentalHistory.builder()
                .rentalRequestId(rentalRequest.getId())
                .postId(post.getId())
                .postStatus(post.getStatus())
                .postCategory(post.getPostCategory())
                .postMemberNickname(post.getMember().getNickname())
                .rentalEndTime(emptySpot.getExpectedCheckoutTime().toLocalDate())
                .location(emptySpot.getLocation())
                .floor(emptySpot.getFloor())
                .seatNumber(emptySpot.getSeatNumber())
                .opponentNickname(opponentNickname)
                .build();
    }

    private static String getOpponentNickname(Member member, Post post, RentalRequest rentalRequest){
        String opponentNickname;
        if (member.getId().equals(post.getMember().getId())){
            return opponentNickname = rentalRequest.getMember().getNickname();
        } else {
            return opponentNickname = post.getMember().getNickname();
        }
    }
}
