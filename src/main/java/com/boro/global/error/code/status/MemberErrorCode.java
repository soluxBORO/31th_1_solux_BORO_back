package com.boro.global.error.code.status;

import com.boro.global.error.code.BaseErrorCode;
import com.boro.global.error.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_404_1", "해당 멤버를 찾을 수 없습니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER_400_1", "이미 존재하는 이메일입니다."),
    MEMBER_ASSET_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_400_2", "보유한 캐릭터 아이템을 찾을 수 없습니다."),
    INVALID_SOOKMYUNG_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER_400_3", "숙명여자대학교 이메일만 사용할 수 있습니다."),
    POINT_INVALID_REQUEST(HttpStatus.BAD_REQUEST, "MEMBER_POINT_400_1", "포인트 지급 요청이 올바르지 않습니다."),
    INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST, "MEMBER_POINT_400_2", "보유 포인트가 부족합니다."),
    SOCIAL_NOT_FOUND(HttpStatus.NOT_FOUND, "SOCIAL_404_1", "해당 소셜를 찾을 수 없습니다."),
    ASSET_NOT_FOUND(HttpStatus.NOT_FOUND, "ASSET_404_1", "해당 캐릭터 아이템을 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .httpStatus(status)
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }
}
