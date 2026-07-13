package com.boro.global.error.code.status;

import com.boro.global.error.code.BaseErrorCode;
import com.boro.global.error.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum EmptySpotErrorCode implements BaseErrorCode {
    INVALID_CHECKOUT_TIME(HttpStatus.BAD_REQUEST, "EMPTYSPOT400_1", "퇴실 예정 시간은 현재 시각으로부터 5분 이내로 설정해야 합니다."),
    EMPTY_SPOT_NOT_FOUND(HttpStatus.NOT_FOUND, "EMPTYSPOT404_1", "해당 빈자리 게시글을 찾을 수 없습니다."),
    NOT_EMPTY_SPOT_OWNER(HttpStatus.FORBIDDEN, "EMPTYSPOT403_1", "본인이 작성한 게시글만 삭제할 수 있습니다.")
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
