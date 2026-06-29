package com.boro.domain.auth.factory;

import com.boro.domain.auth.converter.OAuthConverter;
import com.boro.domain.auth.dto.response.OAuth2ResponseDTO;
import com.boro.domain.auth.factory.dto.GoogleOAuth2ResponseDTO;
import com.boro.domain.member.entity.enums.SocialType;
import com.boro.global.data.OAuth2ConfigData;
import com.boro.global.security.constants.AuthenticationConstants;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GoogleUserLoader extends AbstractOAuth2UserLoader {

    //TOKEN_PREFIX
    private static final SocialType SOCIAL_TYPE = SocialType.GOOGLE;

    public GoogleUserLoader(OAuth2ConfigData oAuth2ConfigData) {
        super(oAuth2ConfigData);
    }

    @Override
    protected String getAccessToken(String code) throws IOException {
        GoogleOAuth2ResponseDTO.Token token = super.getToken(code, GoogleOAuth2ResponseDTO.Token.class);
        return token.access_token();
    }

    @Override
    protected OAuth2ResponseDTO.GetUserInfo getUserInfo(String token) throws IOException {
        GoogleOAuth2ResponseDTO.UserInfo profile = super.getProfile(AuthenticationConstants.TOKEN_PREFIX, token, GoogleOAuth2ResponseDTO.UserInfo.class);
        return OAuthConverter.toGetUserInfo(profile);
    }

    @Override
    public String getSocialType() {
        return SOCIAL_TYPE.name().toLowerCase();
    }

    public String getClientId(){return super.getClientId();}
    public String getRedirectUri(){return super.getRedirectUri();}
}
