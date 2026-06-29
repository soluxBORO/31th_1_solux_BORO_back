package com.boro.global.security.filter;

import com.boro.global.error.ApiResponse;
import com.boro.global.error.code.status.GeneralErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ApiResponse<Void> errorResponse = ApiResponse.onFailure(GeneralErrorCode.UNAUTHORIZED.getCode(), GeneralErrorCode.UNAUTHORIZED.getMessage(), null);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
