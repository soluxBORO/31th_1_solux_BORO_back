package com.boro.global.error.exception.handler;

import com.boro.global.error.code.BaseErrorCode;
import com.boro.global.error.exception.GeneralException;

public class PostException extends GeneralException {
    public PostException(BaseErrorCode code) {
        super(code);
    }
}
