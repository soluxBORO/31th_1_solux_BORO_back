package com.boro.domain.emptyspot.controller;

import com.boro.domain.emptyspot.dto.request.EmptySpotRequestDTO;
import com.boro.domain.emptyspot.dto.response.EmptySpotResponseDTO;
import com.boro.domain.emptyspot.service.command.EmptySpotCommandService;
import com.boro.global.error.ApiResponse;
import com.boro.global.security.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/empty-spot")
@RequiredArgsConstructor
@RestController
@Tag(name = "빈자리 게시글 API")
public class EmptySpotController {

    private final EmptySpotCommandService emptySpotCommandService;

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
}
