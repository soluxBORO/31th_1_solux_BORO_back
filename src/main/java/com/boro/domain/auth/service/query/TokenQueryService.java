package com.boro.domain.auth.service.query;

import com.boro.global.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenQueryService {

    private final JwtUtil jwtUtil;

    public Duration getAccessTokenExpiration() {
        return jwtUtil.getAccessExpiration();
    }

    public Duration getRefreshTokenExpiration() {
        return jwtUtil.getRefreshExpiration();
    }
}
