package com.boro.domain.auth.service.command;

import com.boro.domain.auth.converter.AuthConverter;
import com.boro.domain.auth.dto.response.AuthResponseDTO;
import com.boro.global.security.domain.CustomUserDetails;
import com.boro.global.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenCommandService {

    private final JwtUtil jwtUtil;

    public AuthResponseDTO.TokenResult createLoginToken(CustomUserDetails customUserDetails) {
        return AuthConverter.toTokenResult(
                customUserDetails.getMemberId(),
                jwtUtil.createAccessToken(customUserDetails),
                jwtUtil.createRefreshToken(customUserDetails)
        );
    }

    public String reissueAccessToken(CustomUserDetails customUserDetails) {
        return jwtUtil.createAccessToken(customUserDetails);
    }
}

