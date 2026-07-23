package com.boro.domain.member.converter;

import com.boro.domain.member.dto.response.MemberResponseDTO;
import com.boro.domain.member.entity.Asset;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.entity.MemberAsset;
import com.boro.domain.member.entity.PointHistory;
import com.boro.domain.member.entity.enums.PointReason;
import com.boro.domain.post.entity.Item;
import com.boro.domain.post.entity.Post;
import com.boro.domain.post.entity.PostLike;
import com.boro.domain.rentalrequest.entity.Review;

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
                .profileImageUrl(post.getMember().getProfileUrl())
                .postTitle(item.getTitle())
                .postDescription(item.getDescription())
                .requestCreatedAt(post.getCreatedAt().toLocalDate())
                .price(item.getRentalPrice())
                .priceUnit(item.getRentalPriceUnit())
                .likeCount(post.getPostLikeList().size())
                .build();
    }
}
