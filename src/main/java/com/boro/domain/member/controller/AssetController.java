package com.boro.domain.member.controller;

import com.boro.domain.member.dto.response.MemberResponseDTO;
import com.boro.domain.member.service.command.MemberCommandService;
import com.boro.domain.member.service.query.MemberQueryService;
import com.boro.global.error.ApiResponse;
import com.boro.global.security.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
@RestController
@Tag(name = "눈송이 꾸미기 API")
public class AssetController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @Operation(summary = "상점 상품 조회 API", description = "상점 상품 조회하는 API")
    @GetMapping
    public ApiResponse<List<MemberResponseDTO.StoreAsset>> storeAssetsInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        List<MemberResponseDTO.StoreAsset> storeAssets = memberQueryService.storeAssetsInfo(customUserDetails.getMemberId());
        return ApiResponse.onSuccess(storeAssets);
    }

    @Operation(summary = "상점 구매 API", description = "상점 구매하는 API")
    @PostMapping("/{assetId}")
    public ApiResponse<MemberResponseDTO.CreatedAsset> purchaseStoreAssets(
            @PathVariable Long assetId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        MemberResponseDTO.CreatedAsset createdAsset = memberCommandService.purchaseStoreAssets(customUserDetails.getMemberId(), assetId);
        return ApiResponse.onSuccess(createdAsset);
    }
}
