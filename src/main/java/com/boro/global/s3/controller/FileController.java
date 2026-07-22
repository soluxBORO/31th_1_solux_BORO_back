package com.boro.global.s3.controller;

import com.boro.global.error.ApiResponse;
import com.boro.global.s3.dto.PresignedUrlRequestDTO;
import com.boro.global.s3.dto.PresignedUrlResponseDTO;
import com.boro.global.s3.service.S3PresignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@RestController
@Tag(name = "파일 업로드 API")
public class FileController {

    private final S3PresignService s3PresignService;

    @Operation(summary = "S3 프리사인드 URL 발급 API", description = "이미지 업로드용 프리사인드 URL을 발급합니다")
    @PostMapping("/presigned-url")
    public ApiResponse<PresignedUrlResponseDTO> getPresignedUrl(
            @RequestBody @Valid PresignedUrlRequestDTO request
    ) {
        S3PresignService.PresignedUrlResult result =
                s3PresignService.generatePresignedUrl(request.fileName(), request.contentType());
        return ApiResponse.onSuccess(new PresignedUrlResponseDTO(result.presignedUrl(), result.fileUrl()));
    }
}
