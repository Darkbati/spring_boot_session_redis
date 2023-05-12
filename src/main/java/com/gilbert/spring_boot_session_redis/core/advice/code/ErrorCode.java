package com.gilbert.spring_boot_session_redis.core.advice.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    WRONG_PARAM(HttpStatus.BAD_REQUEST, "잘못된 요청 입니다.");

    private final HttpStatus httpStatus;
    private final String description;
}
