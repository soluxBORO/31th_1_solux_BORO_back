package com.boro.global.security.util;

import com.boro.global.security.domain.CustomUserDetails;
import com.boro.global.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public Authentication getAuthentication(String token) {
        jwtUtil.isValid(token);
        Long memberId = jwtUtil.getMemberId(token);

        CustomUserDetails customUserDetails =
                customUserDetailsService.loadUserByUsername(memberId.toString());

        return new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );
    }
}
