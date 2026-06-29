package com.boro.global.error.exception.handler;

import com.boro.global.error.code.BaseErrorCode;
import com.boro.global.error.exception.GeneralException;

public class AuthException extends GeneralException {
    public AuthException(BaseErrorCode code) {
        super(code);
    }
}
