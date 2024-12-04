package com.jangmo.web.exception.custom;

import com.jangmo.web.constants.message.ErrorMessage;

public class AuthException extends BaseException {
    public AuthException(ErrorMessage error) {
        super(error);
    }
}
