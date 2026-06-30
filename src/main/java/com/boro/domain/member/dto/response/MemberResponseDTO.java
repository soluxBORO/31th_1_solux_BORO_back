package com.boro.domain.member.dto.response;

import lombok.Builder;

public record MemberResponseDTO() {

    @Builder
    public record MemberInfo(
            String email,
            String studentNumber,
            String nickname,
            Integer point
    ){}
}
