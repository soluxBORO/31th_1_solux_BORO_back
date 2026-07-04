package com.boro.domain.post.dto.response;

import com.boro.domain.post.entity.enums.ItemCategory;
import com.boro.domain.post.entity.enums.PostStatus;
import com.boro.domain.post.entity.enums.RentalPriceUnit;
import lombok.Builder;

import java.time.LocalDate;
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
            ItemCategory category,
            String title,
            String description,
            LocalDate rentalStartTime,
            LocalDate rentalEndTime,
            Integer rentalPrice,
            RentalPriceUnit rentalPriceUnit,
            String authorNickname
    ) {}
}
