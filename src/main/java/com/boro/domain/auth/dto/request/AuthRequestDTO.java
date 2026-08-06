package com.boro.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO() {

    public record SignUp(
            @NotBlank
            String signUpToken,
            @NotBlank
            String name,
            @NotBlank
            String nickname,
            @NotBlank
            String studentNumber
    ){}
}
