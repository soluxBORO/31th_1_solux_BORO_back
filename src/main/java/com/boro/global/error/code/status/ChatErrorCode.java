package com.boro.global.error.code.status;

import com.boro.global.error.code.BaseErrorCode;
import com.boro.global.error.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ChatErrorCode implements BaseErrorCode {
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT404_1", "존재하지 않는 채팅방입니다."),
    CHAT_OPPONENT_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT404_2", "채팅 상대방이 없습니다."),
    CHAT_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT404_3", "채팅 메시지가 없습니다."),
    CHAT_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT404_4", "채팅 사용자가 없습니다."),
    INVALID_TEXT_MESSAGE(HttpStatus.BAD_REQUEST, "CHAT400_1", "텍스트 메시지는 이미지를 포함할 수 없습니다."),
    INVALID_IMAGE_MESSAGE(HttpStatus.BAD_REQUEST, "CHAT400_2", "이미지 메시지는 텍스트를 포함할 수 없습니다."),
    CHAT_IMAGE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "CHAT400_3", "채팅 이미지는 최대 3장까지 첨부할 수 있습니다."),
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
