package com.jangmo.web.exception.handler;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.custom.AuthException;
import com.jangmo.web.exception.custom.FieldValidationException;
import com.jangmo.web.exception.custom.InvalidStateException;
import com.jangmo.web.exception.custom.NotFoundException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class CustomExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public Object handleNotFound(NotFoundException ex) {
        return toResponse(ex);
    }

    @ExceptionHandler(AuthException.class)
    public Object handleAuth(AuthException ex) {
        return toResponse(ex);
    }

    @ExceptionHandler(InvalidStateException.class)
    public Object handleInvalidState(InvalidStateException ex) {
        return toResponse(ex);
    }

    @ExceptionHandler(FieldValidationException.class)
    public Object handleFieldValid(FieldValidationException ex) {
        return toResponse(ex.error(), new String[] {ex.getField()});
    };

    @ExceptionHandler(ConstraintViolationException.class)
    public Object handleValidationViolation(ConstraintViolationException ex) {
        log.info("Parameter Error :: {}", ex.getConstraintViolations().size());
        ConstraintViolation<?> violation = ((ConstraintViolation<?>) ex.getConstraintViolations().toArray()[0]);
        String message = "< " + violation.getPropertyPath() + "> " + violation.getMessage();
        return toResponse(ErrorMessage.REQUEST_QUERY_PARAM, new String[] {message});
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidationViolation(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        String message = Objects.requireNonNull(result.getFieldError()).getDefaultMessage();
        return toResponse(ErrorMessage.REQUEST_BODY_FIELD, new String[] {message});
    }
}
