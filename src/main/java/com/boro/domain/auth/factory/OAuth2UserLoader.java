package com.boro.domain.auth.factory;

import com.boro.domain.auth.dto.response.OAuth2ResponseDTO;

public interface OAuth2UserLoader {
    OAuth2ResponseDTO.GetUserInfo loadUser(String code);
    String getSocialType();
}
