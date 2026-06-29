package com.boro.global.security.util;


import com.boro.domain.auth.dto.util.TokenDTO;
import com.boro.domain.member.entity.enums.SocialType;
import com.boro.global.data.JwtConfigData;
import com.boro.global.error.code.status.AuthErrorCode;
import com.boro.global.error.exception.handler.AuthException;
import com.boro.global.security.constants.AuthenticationConstants;
import com.boro.global.security.domain.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    @Getter
    private final Duration accessExpiration;
    @Getter
    private final Duration refreshExpiration;

    public JwtUtil(JwtConfigData jwtConfigData) {
        this.secretKey = Keys.hmacShaKeyFor(jwtConfigData.getSecret().getBytes(StandardCharsets.UTF_8));
        this.accessExpiration = Duration.ofMillis(jwtConfigData.getTime().getAccessToken());
        this.refreshExpiration = Duration.ofMillis(jwtConfigData.getTime().getRefreshToken());
    }

    public String createAccessToken(CustomUserDetails details) {
        return createToken(details, accessExpiration);
    }

    public String createRefreshToken(CustomUserDetails details) {
        return createToken(details, refreshExpiration);
    }

    public Long getMemberId(String token) {
        return getClaims(token).getPayload().get("id", Long.class);
    }

    private String createToken(CustomUserDetails detail, Duration expiration) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(detail.getUsername())
                .claim("id", detail.getMemberId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(secretKey)
                .compact();
    }

    private Jws<Claims> getClaims(String token) throws JwtException {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .clockSkewSeconds(60)
                    .build()
                    .parseSignedClaims(token);

        } catch (ExpiredJwtException e) {
            throw new AuthException(AuthErrorCode.EXPIRED_TOKEN);

        } catch (JwtException e) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    public static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AuthenticationConstants.AUTH_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(AuthenticationConstants.TOKEN_PREFIX)) {
            return bearerToken.substring(AuthenticationConstants.TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean isValid(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String createSignUpToken(
            SocialType socialType,
            String providerId,
            String email,
            String name
    ) {
        Instant now = Instant.now();

        return Jwts.builder()
                .claim("type", "SIGNUP")
                .claim("socialType", socialType.name())
                .claim("providerId", providerId)
                .claim("email", email)
                .claim("name", name)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(Duration.ofMinutes(10))))
                .signWith(secretKey)
                .compact();
    }

    public TokenDTO.SignUpTokenPayload getSignUpPayload(String token) {

        Claims claims = getClaims(token).getPayload();

        SocialType socialType = SocialType.valueOf(claims.get("socialType", String.class));
        String providerId = claims.get("providerId", String.class);
        String email = claims.get("email", String.class);
        String name = claims.get("name", String.class);

        if (!"SIGNUP".equals(claims.get("type"))) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
        if (providerId == null) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }

        return new TokenDTO.SignUpTokenPayload(socialType, providerId, email, name);
    }
}
