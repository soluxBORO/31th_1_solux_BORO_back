package com.boro.domain.member.event;

import com.boro.domain.member.dto.request.MemberRequestDTO;
import com.boro.domain.member.service.command.MemberCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointEventListener {

    private final MemberCommandService memberCommandService;

    @EventListener
    public void handle(MemberRequestDTO.PointGrantEvent event) {
        memberCommandService.applyPoint(event.memberId(), event.pointReason());
    }
}
