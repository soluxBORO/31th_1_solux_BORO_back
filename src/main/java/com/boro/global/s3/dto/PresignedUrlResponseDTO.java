package com.boro.global.s3.dto;

public record PresignedUrlResponseDTO(
        String presignedUrl,
        String fileUrl
) {}
