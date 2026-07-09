package com.boro.domain.member.service.command;

import com.boro.domain.member.converter.MemberConverter;
import com.boro.domain.member.dto.request.MemberRequestDTO;
import com.boro.domain.member.entity.Member;
import com.boro.domain.member.entity.PointHistory;
import com.boro.domain.member.entity.enums.PointReason;
import com.boro.domain.member.repository.MemberRepository;
import com.boro.global.error.code.status.MemberErrorCode;
import com.boro.global.error.exception.handler.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;

    public void changeMemberInfo(Long memberId, MemberRequestDTO.ChangeMemberInfo request){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.changeMemberInfo(request);
    }

    public void applyPoint(Long memberId, PointReason reason) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.applyPoint(reason.getPoint());
        PointHistory pointHistory = MemberConverter.toPointHistory(reason);
        member.addPointHistory(pointHistory);
        log.info("포인트 이벤트 발행 완료!");
    }

}
