package com.boro.domain.emptyspot.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EmptySpotRequestDTO() {

    public record CreateEmptySpot(
            @NotBlank(message = "장소를 입력해야 합니다.")
            String location,

            @NotNull(message = "층을 입력해야 합니다.")
            Integer floor,

            @NotNull(message = "좌석 번호를 입력해야 합니다.")
            Integer seatNumber,

            @NotNull(message = "콘센트 여부를 선택해야 합니다.")
            Boolean hasPowerOutlet,

            @NotNull(message = "창가 여부를 선택해야 합니다.")
            Boolean hasWindowSeat,

            @NotNull(message = "퇴실 예정 시간을 입력해야 합니다.")
            @Future(message = "퇴실 예정 시간은 현재 시각 이후여야 합니다.")
            LocalDateTime expectedCheckoutTime
    ) {}
}
