package com.boro.domain.rentalrequest.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RentalRequestStatus {
    PENDING("요청중"), APPROVED("대여중"), REJECTED("취소됨"), COMPLETED("반납 완료");

    private final String description;
}
