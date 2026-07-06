package com.boro.global.error.exception.handler;

import com.boro.global.error.code.BaseErrorCode;
import com.boro.global.error.exception.GeneralException;

public class RentalRequestException extends GeneralException {
    public RentalRequestException(BaseErrorCode code) {
        super(code);
    }
}
