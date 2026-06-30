package com.boro.domain.auth.dto.util;

import com.boro.domain.member.entity.enums.SocialType;

public record TokenDTO() {
    public record SignUpTokenPayload(
            SocialType socialType,
            String providerId,
            String email,
            String name
    ) {}
}
