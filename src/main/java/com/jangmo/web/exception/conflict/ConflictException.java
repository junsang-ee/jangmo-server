package com.jangmo.web.exception.conflict;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.BaseException;

public abstract class ConflictException extends BaseException {

	public ConflictException(String reason) {
		super(reason);
	}
	public ConflictException(ErrorMessage error) {
		super(error);
	}
}
