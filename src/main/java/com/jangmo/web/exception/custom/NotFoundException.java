package com.jangmo.web.exception.custom;

import com.jangmo.web.constants.message.ErrorMessage;

public class NotFoundException extends BaseException {
    public NotFoundException(ErrorMessage error) {
        super(error);
    }
}
