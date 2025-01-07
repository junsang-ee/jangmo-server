package com.jangmo.web.exception.custom;

import com.jangmo.web.constants.message.ErrorMessage;
import lombok.Getter;

@Getter
public class FieldValidationException extends BaseException {
    private String field;

    public FieldValidationException(ErrorMessage error) {
        super(error);
    }

    public FieldValidationException(ErrorMessage error, String field) {
        super(error);
        this.field = field;
    }
}
