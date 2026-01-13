package com.jangmo.web.exception.handler;

import com.jangmo.web.exception.*;
import com.jangmo.web.exception.handler.base.BaseExceptionHandler;
import com.jangmo.web.model.dto.response.common.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SystemExceptionHandler extends BaseExceptionHandler {

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ApiErrorResponse> handleBase(BaseException ex) {
		log.error("[{}] {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
		return toResponse(ex);
	}

	@ExceptionHandler(FieldValidationException.class)
	public ResponseEntity<ApiErrorResponse> handleFieldValid(FieldValidationException ex) {
		log.warn("[FieldValidationException] field={} message={}", ex.getField(), ex.getMessage());
		return toResponse(ex.error(), new String[] {ex.getField()});
	}

	@ExceptionHandler(DomainFieldValidationException.class)
	public ResponseEntity<ApiErrorResponse> handleDomainField(DomainFieldValidationException ex) {
		log.error("[{}] {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
		return toResponse(ex);
	}

}
