package com.gilbert.spring_boot_session_redis.core.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.InvocationTargetException;

// @Order 어노테이션 활용하여 우선순위를 최상위에 올리지 않으면, 동작하지 않는다. (다른 LIB과 충돌난 것으로 예상된다.)
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalAdviseController {
    @ExceptionHandler(value = {InvocationTargetException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ErrorResponse> handlerInvocationTargetException(InvocationTargetException e) {
        log.error(e.getMessage(), e);
        return ErrorResponse.makeInternal(e);
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ErrorResponse> handlerException(Exception e) {
        log.error(e.getMessage(), e);
        return ErrorResponse.makeInternal(e);
    }
}
