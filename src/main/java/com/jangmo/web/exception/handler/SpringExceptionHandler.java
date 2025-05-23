package com.jangmo.web.exception.handler;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.handler.base.BaseExceptionHandler;
import com.jangmo.web.model.dto.response.common.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class SpringExceptionHandler extends BaseExceptionHandler {


    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ApiErrorResponse> handleException(ServletException ex){
        log.error("Servlet Exception >> ", ex);
        return toResponse(ErrorMessage.INTERNAL_SERVER_ERROR, null);
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<ApiErrorResponse> handleBadSqlGrammarException(BadSqlGrammarException ex) {
        log.error("BadSqlGrammarException >> SQL syntax error", ex);
        return toResponse(ErrorMessage.INTERNAL_SERVER_ERROR, null);
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<ApiErrorResponse> handleDataAccessResourceFailure(DataAccessResourceFailureException ex) {
        log.error("DataAccessResourceFailureException >> Fail DB Connection ", ex);
        return toResponse(ErrorMessage.INTERNAL_SERVER_ERROR, null);
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public ResponseEntity<ApiErrorResponse> handleGenericDataAccessException(Exception ex) {
        log.error("SQL/DataAccessException >> DB Exception", ex);
        return toResponse(ErrorMessage.INTERNAL_SERVER_ERROR, null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationViolation(ConstraintViolationException ex) {
        ConstraintViolation<?> violation = ex.getConstraintViolations().iterator().next();
        String propertyPath = String.valueOf(violation.getPropertyPath());
        String violationMsg = violation.getMessage();
        String message = "<" + propertyPath + "> " + violationMsg;

        log.warn("ConstraintViolationError >> param={} message={}", propertyPath, violationMsg);

        return toResponse(ErrorMessage.REQUEST_QUERY_PARAM, new String[] {message});
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleArgumentException(MethodArgumentTypeMismatchException ex){
        String parameter = ex.getName();
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "Unknown";
        String value = ex.getValue() != null ? ex.getValue().toString() : "null";

        log.warn("ArgumentTypeMismatchError >>> param={} value={} requiredType={}", parameter, value, requiredType);
        log.debug("Exception stack", ex);

        return toResponse(ErrorMessage.REQUEST_QUERY_PARAM, new String[]{parameter + " 타입 오류"});
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleBodyFieldException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        String field = Objects.requireNonNull(result.getFieldError()).getField();
        String message = Objects.requireNonNull(result.getFieldError()).getDefaultMessage();

        log.warn("RequestBodyValidationError >> field={} message={}", field, message);
        log.debug("Exception stack", ex);

        return toResponse(ErrorMessage.REQUEST_BODY_FIELD, new String[] {message});
    }



}
