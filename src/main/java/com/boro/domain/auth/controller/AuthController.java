package com.boro.domain.auth.controller;

import com.boro.domain.auth.dto.request.AuthRequestDTO;
import com.boro.domain.auth.dto.response.AuthResponseDTO;
import com.boro.domain.auth.dto.response.OAuth2ResponseDTO;
import com.boro.domain.auth.service.command.AuthCommandService;
import com.boro.global.error.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
@Tag(name = "인증 API")
public class AuthController {

    private final AuthCommandService authCommandService;

    @Operation(summary = "구글 소셜 로그인 API", description = "구글 소셜 로그인하는 API")
    @GetMapping("/google/callback")
    public ApiResponse<OAuth2ResponseDTO.Login> signUp(HttpServletRequest request, HttpServletResponse response,
                                                       @RequestParam String code){
        OAuth2ResponseDTO.Login login = authCommandService.loginWithOAuth(request, response, code);
        return ApiResponse.onSuccess(login);
    }

    @Operation(summary = "회원가입 API", description = "회원가입하는 API")
    @PostMapping("/sign-up")
    public ApiResponse<AuthResponseDTO.TokenResult> signUp(@RequestBody @Valid AuthRequestDTO.SignUp request){
        AuthResponseDTO.TokenResult tokenResult = authCommandService.signUp(request);
        return ApiResponse.onSuccess(tokenResult);
    }

    @Operation(summary = "Access Token 재발급 API", description = "토큰 재발급 API")
    @PostMapping("/reissue")
    public ApiResponse<AuthResponseDTO.AccessTokenResult> reissue(HttpServletRequest request, HttpServletResponse response){
        AuthResponseDTO.AccessTokenResult accessTokenResult = authCommandService.reissue(request, response);
        return ApiResponse.onSuccess(accessTokenResult);
    }

    @Operation(summary = "닉네임 중복 확인 API", description = "닉네임 중복 확인하는 API")
    @PostMapping("/nicknames-check")
    public ApiResponse<AuthResponseDTO.NicknameCheck> checkNickname(
            @RequestParam String nickname
    ){
        AuthResponseDTO.NicknameCheck nicknameCheck = authCommandService.checkNickname(nickname);
        return ApiResponse.onSuccess(nicknameCheck);
    }

}
