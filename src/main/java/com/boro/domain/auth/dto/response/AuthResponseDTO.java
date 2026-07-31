package com.boro.domain.auth.dto.response;

import lombok.Builder;

public record AuthResponseDTO() {

    @Builder
    public record TokenResult(
            Long memberId,
            String accessToken,
            String refreshToken
    ){}

    @Builder
    public record AccessTokenResult(
            Long memberId,
            String accessToken
    ){
    }

    @Builder
    public record NicknameCheck(
            String nickname,
            boolean available
    ){}
}
