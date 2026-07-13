package com.boro.domain.emptyspot.controller;

import com.boro.domain.emptyspot.dto.request.EmptySpotRequestDTO;
import com.boro.domain.emptyspot.dto.response.EmptySpotResponseDTO;
import com.boro.domain.emptyspot.service.command.EmptySpotCommandService;
import com.boro.domain.emptyspot.service.query.EmptySpotQueryService;
import com.boro.global.error.ApiResponse;
import com.boro.global.security.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/empty-spot")
@RequiredArgsConstructor
@RestController
@Tag(name = "빈자리 게시글 API")
public class EmptySpotController {

    private final EmptySpotCommandService emptySpotCommandService;
    private final EmptySpotQueryService emptySpotQueryService;

    @Operation(summary = "빈자리 게시글 작성 API", description = "실시간 빈자리 양도 게시글을 작성하는 API")
    @PostMapping
    public ApiResponse<EmptySpotResponseDTO.EmptySpotDetail> createEmptySpot(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid EmptySpotRequestDTO.CreateEmptySpot request
    ) {
        EmptySpotResponseDTO.EmptySpotDetail response =
                emptySpotCommandService.createEmptySpot(customUserDetails.getMemberId(), request);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "빈자리 게시글 수정 API", description = "빈자리 양도 게시글을 수정하는 API")
    @PatchMapping("/{emptySpotId}")
    public ApiResponse<Void> updateEmptySpot(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long emptySpotId,
            @RequestBody @Valid EmptySpotRequestDTO.EditEmptySpot request
    ) {
        emptySpotCommandService.updateEmptySpot(customUserDetails.getMemberId(), emptySpotId, request);
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "빈자리 게시글 삭제 API", description = "빈자리 양도 게시글을 삭제하는 API")
    @DeleteMapping("/{emptySpotId}")
    public ApiResponse<Void> deleteEmptySpot(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long emptySpotId
    ) {
        emptySpotCommandService.deleteEmptySpot(customUserDetails.getMemberId(), emptySpotId);
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "빈자리 게시글 리스트 조회 API", description = "실시간으로 양도 가능한 빈자리 게시글 목록을 조회하는 API")
    @GetMapping
    public ApiResponse<List<EmptySpotResponseDTO.EmptySpotSummary>> getEmptySpotList() {
        List<EmptySpotResponseDTO.EmptySpotSummary> response = emptySpotQueryService.getEmptySpotList();
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "빈자리 게시글 상세 조회 API", description = "빈자리 양도 게시글의 상세 정보를 조회하는 API")
    @GetMapping("/{emptySpotId}")
    public ApiResponse<EmptySpotResponseDTO.EmptySpotInfo> getEmptySpotDetail(@PathVariable Long emptySpotId) {
        EmptySpotResponseDTO.EmptySpotInfo response = emptySpotQueryService.getEmptySpotDetail(emptySpotId);
        return ApiResponse.onSuccess(response);
    }
}
