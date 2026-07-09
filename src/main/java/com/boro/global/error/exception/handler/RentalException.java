package com.boro.global.error.exception.handler;

import com.boro.global.error.code.BaseErrorCode;
import com.boro.global.error.exception.GeneralException;

public class RentalException extends GeneralException {
    public RentalException(BaseErrorCode code) {
        super(code);
    }
}

