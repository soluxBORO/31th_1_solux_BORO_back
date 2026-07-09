package com.boro.domain.member.converter;

import com.boro.domain.member.dto.response.MemberResponseDTO;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.entity.PointHistory;
import com.boro.domain.member.entity.enums.PointReason;

public class MemberConverter {

    public static MemberResponseDTO.MemberInfo toMemberInfo(Member member){
        return MemberResponseDTO.MemberInfo.builder()
                .email(member.getEmail())
                .studentNumber(member.getStudentNumber())
                .nickname(member.getNickname())
                .point(member.getPoint())
                .build();
    }

    public static PointHistory toPointHistory(PointReason pointReason){
        return PointHistory.builder()
                .pointReason(pointReason)
                .build();
    }

    public static MemberResponseDTO.PointHistory toPointHistoryDTO(PointHistory pointHistory){
        return MemberResponseDTO.PointHistory.builder()
                .pointDescription(pointHistory.getPointReason().getDescription())
                .point(pointHistory.getPointReason().getPoint())
                .createdAt(pointHistory.getCreatedAt().toLocalDate())
                .build();
    }
}
