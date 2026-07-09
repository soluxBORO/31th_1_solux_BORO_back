package com.boro.domain.member.service.query;

import com.boro.domain.member.converter.MemberConverter;
import com.boro.domain.member.dto.response.MemberResponseDTO;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.repository.MemberRepository;
import com.boro.domain.member.repository.PointHistoryRepository;
import com.boro.global.error.code.status.MemberErrorCode;
import com.boro.global.error.exception.handler.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public Member findById(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public MemberResponseDTO.MemberInfo getMemberInfo(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return MemberConverter.toMemberInfo(member);
    }

    public List<MemberResponseDTO.PointHistory> getPointHistory(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return pointHistoryRepository.findByMemberOrderByCreatedAtDesc(member).stream()
                .map(MemberConverter::toPointHistoryDTO)
                .toList();

    }


}
