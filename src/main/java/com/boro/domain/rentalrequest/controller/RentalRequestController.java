package com.boro.domain.rentalrequest.controller;

import com.boro.domain.rentalrequest.dto.request.RentalRequestRequestDTO;
import com.boro.domain.rentalrequest.dto.response.RentalRequestResponseDTO;
import com.boro.domain.rentalrequest.service.command.RentalRequestCommandService;
import com.boro.domain.rentalrequest.service.query.RentalRequestQueryService;
import com.boro.global.error.ApiResponse;
import com.boro.global.security.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/rental-requests")
@RequiredArgsConstructor
@RestController
@Tag(name = "대여 신청 API")
public class RentalRequestController {

    private final RentalRequestQueryService rentalRequestQueryService;
    private final RentalRequestCommandService rentalRequestCommandService;

    @Operation(summary = "빌린 물품 대여 현황 조회 API", description = "내가 빌린 물품들의 대여 현황을 조회하는 API")
    @GetMapping("/borrowed")
    public ApiResponse<List<RentalRequestResponseDTO.BorrowedItem>> getBorrowedList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<RentalRequestResponseDTO.BorrowedItem> response =
                rentalRequestQueryService.getBorrowedList(customUserDetails.getMemberId());
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "빌려준 물품 대여 현황 조회 API", description = "내가 빌려준 물품들의 대여 현황을 조회하는 API")
    @GetMapping("/lent")
    public ApiResponse<List<RentalRequestResponseDTO.LentItem>> getLentList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<RentalRequestResponseDTO.LentItem> response =
                rentalRequestQueryService.getLentList(customUserDetails.getMemberId());
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "대여 요청 승인/거절 API", description = "대여 요청을 승인하거나 거절하는 API")
    @PatchMapping
    public ApiResponse<Void> decideRentalRequest(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid RentalRequestRequestDTO.Decide request
    ) {
        rentalRequestCommandService.decide(customUserDetails.getMemberId(), request);
        return ApiResponse.onSuccess(null);
    }
}
