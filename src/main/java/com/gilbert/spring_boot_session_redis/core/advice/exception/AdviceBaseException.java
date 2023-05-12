package com.gilbert.spring_boot_session_redis.core.advice.exception;

import com.gilbert.spring_boot_session_redis.core.advice.code.ErrorCode;
import lombok.Getter;

@Getter
public class AdviceBaseException extends RuntimeException {
    private final ErrorCode errorCode;

    public AdviceBaseException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public AdviceBaseException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
