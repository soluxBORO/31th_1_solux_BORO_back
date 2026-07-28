package com.boro.domain.member.converter;

import com.boro.domain.member.dto.response.MemberResponseDTO;
import com.boro.domain.member.entity.Asset;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.entity.MemberAsset;
import com.boro.domain.member.entity.PointHistory;
import com.boro.domain.post.entity.*;
import com.boro.domain.rentalrequest.entity.Review;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class MemberConverter {

    public static MemberResponseDTO.MemberInfo toMemberInfo(Member member){
        return MemberResponseDTO.MemberInfo.builder()
                .email(member.getEmail())
                .studentNumber(member.getStudentNumber())
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

    public static MemberResponseDTO.ReviewDetail toReviewDetail(Member reviewer, Review review){
        return MemberResponseDTO.ReviewDetail.builder()
                .reviewerNickname(reviewer.getNickname())
                .postTitle(review.getRentalRequest().getPost().getItem().getTitle())
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
                .postImageUrl(item.getItemImages().stream().findFirst() .map(ItemImage::getImageUrl).orElse(null))
                .postCategory(post.getPostCategory())
                .postStatus(post.getStatus())
                .postTitle(item.getTitle())
                .postDescription(item.getDescription())
                .requestCreatedAt(post.getCreatedAt().toLocalDate())
                .profileImageUrl(post.getMember().getProfileUrl())
                .price(item.getRentalPrice())
                .priceUnit(item.getRentalPriceUnit())
                .likeCount(post.getPostLikeList().size())
                .build();
    }

    public static MemberResponseDTO.MyPost toMyItemPost(Post post){
        Item item = post.getItem();
        return MemberResponseDTO.MyPost.builder()
                .postStatus(post.getStatus())
                .postCategory(post.getPostCategory())
                .price(item.getRentalPrice())
                .priceUnit(item.getRentalPriceUnit())
                .postTitle(item.getTitle())
                .postDescription(item.getDescription())
//                .requestCreatedAt(post.getCreatedAt().toLocalDate())
                .build();
    }

    public static MemberResponseDTO.MyPost toMyEmptySpotPost(Post post){

        EmptySpot emptySpot = post.getEmptySpot();
        long leftMinutes = Math.max(0, Duration.between(
                LocalDateTime.now(), emptySpot.getExpectedCheckoutTime()).toMinutes()
        );
        return MemberResponseDTO.MyPost.builder()
                .postStatus(post.getStatus())
                .postCategory(post.getPostCategory())
                .location(emptySpot.getLocation())
                .floor(emptySpot.getFloor())
                .seatNumber(emptySpot.getSeatNumber())
//                .requestCreatedAt(post.getCreatedAt().toLocalDate())
                .leftMinutes(leftMinutes)
                .build();
    }
}
