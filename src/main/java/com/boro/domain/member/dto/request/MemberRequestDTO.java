package com.boro.domain.member.dto.request;

public record MemberRequestDTO() {

    public record ChangeMemberInfo(
        String nickname
    ){}
}
