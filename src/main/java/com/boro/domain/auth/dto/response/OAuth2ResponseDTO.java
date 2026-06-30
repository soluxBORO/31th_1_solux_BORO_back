package com.boro.domain.auth.dto.response;

import com.boro.domain.auth.entity.enums.AuthStatus;
import com.boro.domain.member.entity.enums.SocialType;
import lombok.Builder;

public record OAuth2ResponseDTO() {

    @Builder
    public record Login(
        AuthStatus authStatus,
        String accessToken,
        String refreshToken,
        String signUpToken,
        String email,
        String name
    ){}

    @Builder
    public record GetUserInfo(
            String name,
            String email,
            String providerId,
            SocialType socialType
    ){}
}
