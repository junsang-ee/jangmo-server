package com.jangmo.web.exception;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.BaseException;

public class InvalidStateException extends BaseException {
	public InvalidStateException(ErrorMessage error) {
		super(error);
	}
}
