package com.boro.global.error.exception.handler;

import com.boro.global.error.code.BaseErrorCode;
import com.boro.global.error.exception.GeneralException;

public class OAuthException extends GeneralException {
    public OAuthException(BaseErrorCode code) {
        super(code);
    }
}
