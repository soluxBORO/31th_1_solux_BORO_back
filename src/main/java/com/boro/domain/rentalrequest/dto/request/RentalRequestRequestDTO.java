package com.boro.domain.rentalrequest.dto.request;

import com.boro.domain.rentalrequest.entity.enums.ReviewSentiment;
import jakarta.validation.constraints.NotBlank;

public record RentalRequestRequestDTO() {

    public record Review(
            @NotBlank
            ReviewSentiment reviewSentiment,
            String content
    ){}
}
