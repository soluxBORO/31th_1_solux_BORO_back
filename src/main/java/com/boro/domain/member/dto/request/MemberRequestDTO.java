package com.boro.domain.member.dto.request;

import com.boro.domain.member.entity.enums.PointReason;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberRequestDTO() {

    public record ChangeMemberInfo(
        String nickname,
        String phoneNumber
    ){}

    public record PointGrantEvent(
            Long memberId,
            PointReason pointReason
    ){}

    public record MemberAssetEquipRequest(
            @NotNull
            Boolean equipped
    ) {
    }
}
