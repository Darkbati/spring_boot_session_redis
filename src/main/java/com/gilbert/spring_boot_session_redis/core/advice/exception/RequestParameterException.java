package com.gilbert.spring_boot_session_redis.core.advice.exception;

import com.gilbert.spring_boot_session_redis.core.advice.code.ErrorCode;

public class RequestParameterException extends AdviceBaseException {
    public RequestParameterException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RequestParameterException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
