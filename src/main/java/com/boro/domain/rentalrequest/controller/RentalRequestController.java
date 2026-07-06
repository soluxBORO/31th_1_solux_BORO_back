package com.boro.domain.rentalrequest.controller;

import com.boro.domain.rentalrequest.dto.response.RentalRequestResponseDTO;
import com.boro.domain.rentalrequest.service.query.RentalRequestQueryService;
import com.boro.global.error.ApiResponse;
import com.boro.global.security.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/rental-requests")
@RequiredArgsConstructor
@RestController
@Tag(name = "대여 신청 API")
public class RentalRequestController {

    private final RentalRequestQueryService rentalRequestQueryService;

    @Operation(summary = "빌린 물품 대여 현황 조회 API", description = "내가 빌린 물품들의 대여 현황을 조회하는 API")
    @GetMapping("/borrowed")
    public ApiResponse<List<RentalRequestResponseDTO.BorrowedItem>> getBorrowedList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<RentalRequestResponseDTO.BorrowedItem> response =
                rentalRequestQueryService.getBorrowedList(customUserDetails.getMemberId());
        return ApiResponse.onSuccess(response);
    }
}
