package com.boro.domain.rentalrequest.dto.request;

import com.boro.domain.rentalrequest.entity.enums.ReviewSentiment;

public record RentalRequestRequestDTO() {

    public record Review(
            ReviewSentiment reviewSentiment,
            String content
    ){}
}
