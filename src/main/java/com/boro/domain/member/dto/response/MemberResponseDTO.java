package com.boro.domain.member.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public record MemberResponseDTO() {

    @Builder
    public record MemberInfo(
            String email,
            String studentNumber,
            String nickname,
            Integer point
    ){}

    @Builder
    public record PointHistory(
            String pointDescription,
            Integer point,
            LocalDate createdAt
    ){}

    @Builder
    public record Review(
            Integer likeCount,
            Integer dislikeCount,
            List<ReviewDetail> reviewDetailList
    ){}

    @Builder
    public record ReviewDetail(
            String reviewerNickname,
            String postTitle,
            LocalDate createdAt,
            String content
    ){}
}
