package com.jangmo.web.exception.custom;

import com.jangmo.web.constants.message.ErrorMessage;

public class InvalidStateException extends BaseException {
    public InvalidStateException(ErrorMessage error) {
        super(error);
    }
}
