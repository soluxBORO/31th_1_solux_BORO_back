package com.boro.domain.auth.converter;

import com.boro.domain.auth.dto.request.AuthRequestDTO;
import com.boro.domain.auth.dto.response.AuthResponseDTO;
import com.boro.domain.auth.dto.util.TokenDTO;
import com.boro.domain.auth.entity.Social;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.entity.enums.SocialType;

public class AuthConverter {

    public static AuthResponseDTO.TokenResult toTokenResult(Long memberId, String accessToken, String refreshToken) {
        return AuthResponseDTO.TokenResult.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static Social toSocial(SocialType socialType, TokenDTO.SignUpTokenPayload payload){
        return Social.builder()
                .socialType(socialType)
                .providerId(payload.providerId())
                .email(payload.email())
                .build();
    }

    public static Member toMember(TokenDTO.SignUpTokenPayload payload, AuthRequestDTO.SignUp request){
        return Member.builder()
                .nickname(request.nickname())
                .studentNumber(request.studentNumber())
                .name(payload.name())
                .email(payload.email())
                .build();
    }

    public static AuthResponseDTO.AccessTokenResult toAccessTokenResult(Long memberId, String accessToken) {
        return AuthResponseDTO.AccessTokenResult.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .build();
    }

    public static AuthResponseDTO.NicknameCheck toNicknameCheck(
            String nickname, boolean exists
    ){
        return AuthResponseDTO.NicknameCheck.builder()
                .nickname(nickname)
                .available(!exists)
                .build();
    }
}
