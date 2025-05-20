package com.jangmo.web.exception;

import com.jangmo.web.constants.message.ErrorMessage;

public class AuthException extends BaseException {
    public AuthException(String reason) {
        super(reason);
    }

    public AuthException(ErrorMessage error) {
        super(error);
    }
}
