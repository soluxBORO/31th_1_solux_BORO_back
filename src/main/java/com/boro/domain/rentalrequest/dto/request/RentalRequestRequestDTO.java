package com.boro.domain.rentalrequest.dto.request;

import jakarta.validation.constraints.NotNull;

public record RentalRequestRequestDTO() {

    public enum Decision {
        APPROVE, REJECT
    }

    public record Decide(
            @NotNull(message = "대여 요청 ID를 입력해야 합니다.")
            Long rentalRequestId,

            @NotNull(message = "승인/거절 여부를 선택해야 합니다.")
            Decision decision
    ) {}

    public record Complete(
            @NotNull(message = "대여 요청 ID를 입력해야 합니다.")
            Long rentalRequestId
    ) {}
}
