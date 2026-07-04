package com.boro.global.error.code.status;

import com.boro.global.error.code.BaseErrorCode;
import com.boro.global.error.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements BaseErrorCode {
    INVALID_RENTAL_PERIOD(HttpStatus.BAD_REQUEST, "POST400_1", "대여 종료 날짜는 대여 시작 날짜보다 빠를 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST404_1", "해당 게시글을 찾을 수 없습니다.")
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
