package com.boro.domain.member.controller;

import com.boro.domain.auth.service.command.AuthCommandService;
import com.boro.domain.member.dto.request.MemberRequestDTO;
import com.boro.domain.member.dto.response.MemberResponseDTO;
import com.boro.domain.member.service.command.MemberCommandService;
import com.boro.domain.member.service.query.MemberQueryService;
import com.boro.global.error.ApiResponse;
import com.boro.global.security.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@RestController
@Tag(name = "회원 API")
public class MemberController {

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final AuthCommandService authCommandService;

    @Operation(summary = "멤버 정보 조회 API", description = "멤버의 정보를 조회하는 API")
    @GetMapping
    public ApiResponse<MemberResponseDTO.MemberInfo> getMemberInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        MemberResponseDTO.MemberInfo memberInfo = memberQueryService.getMemberInfo(customUserDetails.getMemberId());
        return ApiResponse.onSuccess(memberInfo);
    }

    @Operation(summary = "멤버 정보 수정 API", description = "멤버의 정보를 수정하는 API")
    @PutMapping
    public ApiResponse<Void> changeMemberInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid MemberRequestDTO.ChangeMemberInfo request
    ){
        memberCommandService.changeMemberInfo(customUserDetails.getMemberId(), request);
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃하는 API")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            HttpServletRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        authCommandService.logout(request, customUserDetails.getMemberId());
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "탈퇴 API", description = "탈퇴하는 API")
    @DeleteMapping("/withdraw")
    public ApiResponse<Void> withdraw(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        authCommandService.withdraw(customUserDetails.getMemberId());
        return ApiResponse.onSuccess(null);
    }
}
