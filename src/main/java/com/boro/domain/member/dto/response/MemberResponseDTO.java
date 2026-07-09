package com.boro.domain.member.dto.response;

import lombok.Builder;

import java.time.LocalDate;

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
}
