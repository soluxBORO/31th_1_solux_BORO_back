package com.boro.domain.member.converter;

import com.boro.domain.member.dto.response.MemberResponseDTO;
import com.boro.domain.member.entity.Member;

public class MemberConverter {

    public static MemberResponseDTO.MemberInfo toMemberInfo(Member member){
        return MemberResponseDTO.MemberInfo.builder()
                .email(member.getEmail())
                .studentNumber(member.getStudentNumber())
                .nickname(member.getNickname())
                .point(member.getPoint())
                .build();
    }
}
