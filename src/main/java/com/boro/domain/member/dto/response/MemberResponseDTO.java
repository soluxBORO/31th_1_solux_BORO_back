package com.boro.domain.member.dto.response;

import com.boro.domain.member.entity.enums.AssetCategory;
import com.boro.domain.post.entity.enums.PostCategory;
import com.boro.domain.post.entity.enums.PostStatus;
import com.boro.domain.post.entity.enums.RentalPriceUnit;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public record MemberResponseDTO() {

    @Builder
    public record MemberInfo(
            String email,
            String studentNumber,
            String nickname,
            Integer point
    ){}

    @Builder
    public record PointHistory(
            String pointDescription,
            Integer point,
            LocalDate createdAt
    ){}

    @Builder
    public record Review(
            Integer likeCount,
            Integer dislikeCount,
            List<ReviewDetail> reviewDetailList
    ){}

    @Builder
    public record ReviewDetail(
            String reviewerNickname,
            String postTitle,
            LocalDate createdAt,
            String content
    ){}

    @Builder
    public record StoreAsset(
            Long itemId,
            String itemName,
            AssetCategory itemCategory,
            Integer itemPrice,
            boolean owned
    ){}

    @Builder
    public record CreatedAsset(
            Long assetId
    ){}

    @Builder
    public record MemberAsset(
            Long itemId,
            String itemName,
            AssetCategory itemCategory,
            boolean equipped
    ){}

    @Builder
    public record MemberLikePost(
            String postImageUrl,
            PostCategory postCategory,
            PostStatus postStatus,
            String postTitle,
            String postDescription,
            LocalDate requestCreatedAt,
            String profileImageUrl,
            String postMemberNickname,
            int price,
            RentalPriceUnit priceUnit,
            int likeCount
    ){}

    @Builder
    public record MyPost(
            PostStatus postStatus,
            PostCategory postCategory,
            int price,
            RentalPriceUnit priceUnit,
            String postTitle,
            String postDescription,
            LocalDate requestCreatedAt,
            Long leftMinutes,
            String location,
            Integer floor,
            Integer seatNumber
    ){}
}
