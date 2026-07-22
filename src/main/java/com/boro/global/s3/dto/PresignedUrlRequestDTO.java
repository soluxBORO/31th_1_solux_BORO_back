package com.boro.global.s3.dto;

import jakarta.validation.constraints.NotBlank;

public record PresignedUrlRequestDTO(
        @NotBlank String fileName,
        @NotBlank String contentType
) {}
