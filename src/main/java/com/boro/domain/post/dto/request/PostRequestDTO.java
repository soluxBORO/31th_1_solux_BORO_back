package com.boro.domain.post.dto.request;

import com.boro.domain.post.entity.enums.ItemCategory;
import com.boro.domain.post.entity.enums.RentalPriceUnit;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record PostRequestDTO() {

    public record CreatePost(
            @NotEmpty(message = "사진을 최소 1장 이상 첨부해야 합니다.")
            @Size(max = 10, message = "사진은 최대 10장까지 첨부할 수 있습니다.")
            List<@NotBlank String> imageUrlList,

            @NotNull(message = "카테고리를 선택해야 합니다.")
            ItemCategory category,

            @NotBlank(message = "제목을 입력해야 합니다.")
            @Size(max = 50, message = "제목은 최대 50자까지 입력할 수 있습니다.")
            String title,

            @NotBlank(message = "대여물품 설명을 입력해야 합니다.")
            @Size(max = 1000, message = "설명은 최대 1000자까지 입력할 수 있습니다.")
            String description,

            @NotNull(message = "대여 시작 날짜를 입력해야 합니다.")
            LocalDate rentalStartTime,

            @NotNull(message = "대여 종료 날짜를 입력해야 합니다.")
            LocalDate rentalEndTime,

            @NotNull(message = "대여 비용을 입력해야 합니다.")
            @Min(value = 0, message = "대여 비용은 0원 이상이어야 합니다.")
            @Max(value = 5000, message = "대여 비용은 최대 5,000원까지 입력할 수 있습니다.")
            Integer rentalPrice,

            @NotNull(message = "대여 비용 단위를 선택해야 합니다.")
            RentalPriceUnit rentalPriceUnit
    ) {}

    public record EditPost(
            @NotEmpty(message = "사진을 최소 1장 이상 첨부해야 합니다.")
            @Size(max = 10, message = "사진은 최대 10장까지 첨부할 수 있습니다.")
            List<@NotBlank String> imageUrlList,

            @NotNull(message = "카테고리를 선택해야 합니다.")
            ItemCategory category,

            @NotBlank(message = "제목을 입력해야 합니다.")
            @Size(max = 50, message = "제목은 최대 50자까지 입력할 수 있습니다.")
            String title,

            @NotBlank(message = "대여물품 설명을 입력해야 합니다.")
            @Size(max = 1000, message = "설명은 최대 1000자까지 입력할 수 있습니다.")
            String description,

            @NotNull(message = "대여 시작 날짜를 입력해야 합니다.")
            LocalDate rentalStartTime,

            @NotNull(message = "대여 종료 날짜를 입력해야 합니다.")
            LocalDate rentalEndTime,

            @NotNull(message = "대여 비용을 입력해야 합니다.")
            @Min(value = 0, message = "대여 비용은 0원 이상이어야 합니다.")
            @Max(value = 5000, message = "대여 비용은 최대 5,000원까지 입력할 수 있습니다.")
            Integer rentalPrice,

            @NotNull(message = "대여 비용 단위를 선택해야 합니다.")
            RentalPriceUnit rentalPriceUnit
    ) {}
}
