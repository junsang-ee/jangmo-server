package com.jangmo.web.exception.handler;

import com.jangmo.web.exception.*;
import com.jangmo.web.exception.handler.base.BaseExceptionHandler;
import com.jangmo.web.model.dto.response.common.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(annotations = RestController.class)
public class SystemExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex) {
        log.error("[{}] {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return toResponse(ex);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiErrorResponse> handleAuth(AuthException ex) {
        log.error("[{}] {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return toResponse(ex);
    }

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicated(DuplicatedException ex) {
        log.error("[{}] {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return toResponse(ex);
    }

    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidState(InvalidStateException ex){
        log.error("[{}] {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return toResponse(ex);
    }

    @ExceptionHandler(FieldValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleFieldValid(FieldValidationException ex) {
        log.warn("[FieldValidationException] field={} message={}", ex.getField(), ex.getMessage());
        return toResponse(ex.error(), new String[] {ex.getField()});
    };


}
