package com.boro.domain.auth.converter;

import com.boro.domain.auth.dto.response.OAuth2ResponseDTO;
import com.boro.domain.auth.entity.enums.AuthStatus;
import com.boro.domain.auth.factory.dto.GoogleOAuth2ResponseDTO;
import com.boro.domain.member.entity.enums.SocialType;

public class OAuthConverter {

    public static OAuth2ResponseDTO.GetUserInfo toGetUserInfo(GoogleOAuth2ResponseDTO.UserInfo google){
        return OAuth2ResponseDTO.GetUserInfo.builder()
                .name(google.name())
                .email(google.email())
                .providerId(google.id())
                .socialType(SocialType.GOOGLE)
                .build();
    }

    public static OAuth2ResponseDTO.Login toLogin(String accessToken, String refreshToken){
        return OAuth2ResponseDTO.Login.builder()
                .authStatus(AuthStatus.LOGIN)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static OAuth2ResponseDTO.Login toNeedSignUp(String signUpToken, String email, String name){
        return OAuth2ResponseDTO.Login.builder()
                .authStatus(AuthStatus.NEED_SIGNUP)
                .signUpToken(signUpToken)
                .email(email)
                .name(name)
                .build();
    }
}
