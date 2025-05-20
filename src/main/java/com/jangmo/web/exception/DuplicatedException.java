package com.jangmo.web.exception;

import com.jangmo.web.constants.message.ErrorMessage;

public class DuplicatedException extends BaseException {

    public DuplicatedException(String reason) {
        super(reason);
    }

    public DuplicatedException(ErrorMessage error) {
        super(error);
    }
}
