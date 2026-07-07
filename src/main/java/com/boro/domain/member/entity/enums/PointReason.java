package com.boro.domain.member.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointReason {
    // 적립 조건
    GOOD_REVIEW("거래 완료 후 '좋았어요!' 후기 받음", 300, PointType.EARN),
    EMPTY_SEAT_TRANSFER("빈자리 양도 완료", 100, PointType.EARN),
    ITEM_RENTAL_COMPLETE("물품 대여 완료 (1일당)", 50, PointType.EARN),
    SIGNUP("신규 가입", 500, PointType.EARN),
    PROFILE_VERIFICATION("프로필 인증 완료", 200, PointType.EARN),

    // 차감 조건
    BAD_REVIEW("거래 완료 후 '별로였어요' 후기 받음", -350, PointType.DEDUCT),
    NO_SHOW("노쇼 (약속 불이행)", -500, PointType.DEDUCT),
    ITEM_DAMAGE("물품 파손/훼손", -400, PointType.DEDUCT),
    DELETE_FRIEND_THREE_TIMES("신고 누적 (3회)", -1000, PointType.DEDUCT);

    private final String description;
    private final int point;
    private final PointType type;
}
