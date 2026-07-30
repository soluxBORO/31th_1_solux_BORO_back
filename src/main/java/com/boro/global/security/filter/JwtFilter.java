package com.boro.global.security.filter;

import com.boro.domain.auth.service.query.RedisStorageQueryService;
import com.boro.global.error.code.status.AuthErrorCode;
import com.boro.global.error.exception.handler.AuthException;
import com.boro.global.security.service.CustomUserDetailsService;
import com.boro.global.security.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisStorageQueryService redisStorageQueryService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("[JWT] {} {}", request.getMethod(), request.getRequestURI());

        String token = jwtUtil.resolveToken(request);
        try {
            if (StringUtils.hasText(token) && isValid(token)) {
                Long memberId = jwtUtil.getMemberId(token);
                UserDetails customUserDetails = customUserDetailsService.loadUserByUsername(memberId.toString());
                Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            throw new AuthException(AuthErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (MalformedJwtException e) {
            throw new AuthException(AuthErrorCode.MALFORMED_ACCESS_TOKEN);
        } catch (JwtException e) {
            throw new AuthException(AuthErrorCode.INVALID_ACCESS_TOKEN);
        }
        filterChain.doFilter(request, response);
    }

    private boolean isValid(String token){
        return jwtUtil.isValid(token) && jwtUtil.getMemberId(token) != null && !redisStorageQueryService.isBlackList(token);
    }
}
