package com.boro.global.error.code.status;

import com.boro.global.error.code.BaseErrorCode;
import com.boro.global.error.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RentalRequestErrorCode implements BaseErrorCode {
    RENTAL_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "RENTAL404_1", "해당 대여 요청을 찾을 수 없습니다."),
    NOT_REQUEST_OWNER(HttpStatus.FORBIDDEN, "RENTAL403_1", "본인 게시글에 대한 요청만 처리할 수 있습니다."),
    ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "RENTAL400_1", "이미 처리된 요청입니다.")
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
