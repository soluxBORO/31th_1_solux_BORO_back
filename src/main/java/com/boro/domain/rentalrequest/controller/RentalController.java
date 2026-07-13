package com.boro.domain.rentalrequest.controller;

import com.boro.domain.rentalrequest.dto.request.RentalRequestRequestDTO;
import com.boro.domain.rentalrequest.dto.response.RentalRequestResponseDTO;
import com.boro.domain.rentalrequest.service.command.RentalRequestCommandService;
import com.boro.global.error.ApiResponse;
import com.boro.global.security.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/rentals")
@RequiredArgsConstructor
@RestController
@Tag(name = "대여 신청 API")
public class RentalController {

    private final RentalRequestCommandService rentalRequestCommandService;

    @Operation(summary = "반납 완료 처리 API", description = "대여 중인 물품의 반납을 완료 처리하는 API")
    @PatchMapping("/{rentalId}")
    public ApiResponse<RentalRequestResponseDTO.DecisionResult> completeReturn(
            @PathVariable Long rentalId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        RentalRequestResponseDTO.DecisionResult decisionResult = rentalRequestCommandService.completeReturn(customUserDetails.getMemberId(), rentalId);
        return ApiResponse.onSuccess(decisionResult);
    }

    @Operation(summary = "리뷰 생성 API", description = "대여 반납 완료 후, 리뷰 생성하는 API")
    @PostMapping("/{rentalId}/review")
    public ApiResponse<RentalRequestResponseDTO.CreatedReview> createReview(
            @PathVariable Long rentalId,
            @RequestBody RentalRequestRequestDTO.Review review,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        RentalRequestResponseDTO.CreatedReview createdReview = rentalRequestCommandService.createReview(customUserDetails.getMemberId(), rentalId, review);
        return ApiResponse.onSuccess(createdReview);
    }
}
