package com.boro.domain.post.dto.response;

import com.boro.domain.post.entity.enums.PostCategory;
import com.boro.domain.post.entity.enums.PostStatus;
import com.boro.domain.post.entity.enums.RentalPriceUnit;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PostResponseDTO() {

    @Builder
    public record CreatePost(
            Long postId
    ) {}

    @Builder
    public record PostSummary(
            Long postId,
            PostStatus status,
            List<String> imageUrlList,
            PostCategory category,
            String title,
            String description,
            LocalDate rentalStartTime,
            LocalDate rentalEndTime,
            Integer rentalPrice,
            RentalPriceUnit rentalPriceUnit,
            String authorNickname,
            long likeCount,
            boolean liked,
            LocalDateTime createdAt
    ) {}

    @Builder
    public record PostDetail(
            Long postId,
            PostStatus status,
            List<String> imageUrlList,
            PostCategory category,
            String title,
            String description,
            LocalDate rentalStartTime,
            LocalDate rentalEndTime,
            Integer rentalPrice,
            RentalPriceUnit rentalPriceUnit,
            String authorNickname,
            long likeCount,
            boolean liked,
            LocalDateTime createdAt
    ) {}

    @Builder
    public record LikeResult(
            boolean liked,
            long likeCount
    ) {}
}
