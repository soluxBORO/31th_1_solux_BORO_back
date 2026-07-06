package com.boro.domain.rentalrequest.controller;

import com.boro.domain.rentalrequest.dto.request.RentalRequestRequestDTO;
import com.boro.domain.rentalrequest.service.command.RentalRequestCommandService;
import com.boro.global.error.ApiResponse;
import com.boro.global.security.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/rental")
@RequiredArgsConstructor
@RestController
@Tag(name = "대여 신청 API")
public class RentalController {

    private final RentalRequestCommandService rentalRequestCommandService;

    @Operation(summary = "반납 완료 처리 API", description = "대여 중인 물품의 반납을 완료 처리하는 API")
    @PatchMapping
    public ApiResponse<Void> completeReturn(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid RentalRequestRequestDTO.Complete request
    ) {
        rentalRequestCommandService.completeReturn(customUserDetails.getMemberId(), request);
        return ApiResponse.onSuccess(null);
    }
}
