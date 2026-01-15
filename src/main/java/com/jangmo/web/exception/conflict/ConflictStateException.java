package com.jangmo.web.exception.conflict;

import com.jangmo.web.constants.message.ErrorMessage;

public class ConflictStateException extends ConflictException {

	public ConflictStateException(ErrorMessage error) {
		super(error);
	}
}
