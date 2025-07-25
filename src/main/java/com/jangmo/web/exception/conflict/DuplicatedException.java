package com.jangmo.web.exception.conflict;

import com.jangmo.web.constants.message.ErrorMessage;

public class DuplicatedException extends ConflictException {

    public DuplicatedException(String reason) {
        super(reason);
    }

    public DuplicatedException(ErrorMessage error) {
        super(error);
    }
}
