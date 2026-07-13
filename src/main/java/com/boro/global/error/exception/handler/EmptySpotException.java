package com.boro.global.error.exception.handler;

import com.boro.global.error.code.BaseErrorCode;
import com.boro.global.error.exception.GeneralException;

public class EmptySpotException extends GeneralException {
    public EmptySpotException(BaseErrorCode code) {
        super(code);
    }
}
