package com.boro.domain.member.dto.request;

import com.boro.domain.member.entity.enums.PointReason;

public record MemberRequestDTO() {

    public record ChangeMemberInfo(
        String nickname
    ){}

    public record PointGrantEvent(
            Long memberId,
            PointReason pointReason
    ){}
}
