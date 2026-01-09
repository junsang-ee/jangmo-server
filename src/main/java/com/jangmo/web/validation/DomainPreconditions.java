package com.jangmo.web.validation;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.DomainFieldValidationException;

public final class DomainPreconditions {

	public static void validate(boolean expression, String message) {
		if (!expression) {
			throw new DomainFieldValidationException(
				ErrorMessage.INVALID_DOMAIN_FIELD, message
			);
		}
	}
}
