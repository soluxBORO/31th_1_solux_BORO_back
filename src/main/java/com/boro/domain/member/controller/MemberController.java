package com.boro.domain.member.controller;

import com.boro.domain.auth.service.command.AuthCommandService;
import com.boro.domain.member.dto.request.MemberRequestDTO;
import com.boro.domain.member.dto.response.MemberResponseDTO;
import com.boro.domain.member.service.command.MemberCommandService;
import com.boro.domain.member.service.query.MemberQueryService;
import com.boro.domain.rentalrequest.entity.enums.ReviewSentiment;
import com.boro.global.error.ApiResponse;
import com.boro.global.security.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "포인트 이력 조회 API", description = "포인트 이력 조회하는 API")
    @GetMapping("/points")
    public ApiResponse<List<MemberResponseDTO.PointHistory>> getPointHistory(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<MemberResponseDTO.PointHistory> pointHistory = memberQueryService.getPointHistory(customUserDetails.getMemberId());
        return ApiResponse.onSuccess(pointHistory);
    }

    @Operation(summary = "작성한 대여 후기 리스트 조회 API", description = "내가 작성한 대여 후기 리스트 조회하는 API")
    @GetMapping("/reviews/written")
    public ApiResponse<MemberResponseDTO.Review> getReceivedReviews(
            ReviewSentiment reviewSentiment,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        MemberResponseDTO.Review writtenReviews = memberQueryService.getWrittenReviews(reviewSentiment, customUserDetails.getMemberId());
        return ApiResponse.onSuccess(writtenReviews);
    }

    @Operation(summary = "받은 대여 후기 리스트 조회 API", description = "내가 받은 대여 후기 리스트 조회하는 API")
    @GetMapping("/reviews/received")
    public ApiResponse<MemberResponseDTO.Review> getWrittenReviews(
            ReviewSentiment reviewSentiment,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        MemberResponseDTO.Review receivedReviews = memberQueryService.getReceivedReviews(reviewSentiment, customUserDetails.getMemberId());
        return ApiResponse.onSuccess(receivedReviews);
    }

    @Operation(summary = "내가 보유한 캐릭터 아이템 조회 API", description = "내가 보유한 캐릭터 아이템 조회하는 API")
    @GetMapping("/assets")
    public ApiResponse<List<MemberResponseDTO.MemberAsset>> getMemberAssets(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        List<MemberResponseDTO.MemberAsset> memberAssetsInfo = memberQueryService.getMemberAssetsInfo(customUserDetails.getMemberId());
        return ApiResponse.onSuccess(memberAssetsInfo);
    }

    @Operation(summary = "꾸미기 장착/해제 API", description = "내가 보유한 캐릭터 아이템 장착/해제하는 API")
    @PatchMapping("/assets/{assetId}/equips")
    public ApiResponse<MemberResponseDTO.MemberAsset> equipMemberAsset(
            @PathVariable Long assetId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid MemberRequestDTO.MemberAssetEquipRequest request
    ){
        MemberResponseDTO.MemberAsset memberAsset = memberCommandService.equipMemberAsset(customUserDetails.getMemberId(), assetId, request);
        return ApiResponse.onSuccess(memberAsset);
    }

    @Operation(summary = "좋아요한 게시물 조회 API", description = "내가 좋아요한 게시물 조회하는 API")
    @GetMapping("/liked-posts")
    public ApiResponse<List<MemberResponseDTO.MemberLikePost>> getLikePosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        List<MemberResponseDTO.MemberLikePost> likePosts = memberQueryService.getLikePosts(customUserDetails.getMemberId());
        return ApiResponse.onSuccess(likePosts);
    }
}
