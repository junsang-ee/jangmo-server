package com.jangmo.web.exception;

import com.jangmo.web.constants.message.ErrorMessage;
import lombok.Getter;

@Getter
public class DomainFieldValidationException extends BaseException {

    private String errorMessage;

    public DomainFieldValidationException(ErrorMessage error) {
        super(error);
    }

    public DomainFieldValidationException(ErrorMessage error, String message) {
        super(error);
        this.errorMessage = error.messageFormat(message);
    }
}
