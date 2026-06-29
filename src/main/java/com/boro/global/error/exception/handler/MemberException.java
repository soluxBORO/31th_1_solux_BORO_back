package com.boro.global.error.exception.handler;

import com.boro.global.error.code.BaseErrorCode;
import com.boro.global.error.exception.GeneralException;

public class MemberException extends GeneralException {
    public MemberException(BaseErrorCode code) {
        super(code);
    }
}
