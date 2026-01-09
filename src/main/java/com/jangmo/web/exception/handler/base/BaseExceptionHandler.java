package com.jangmo.web.exception.handler.base;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.BaseException;
import com.jangmo.web.model.dto.response.common.ApiErrorResponse;
import com.jangmo.web.utils.MessageUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

public class BaseExceptionHandler {

	public String getMessage(String target) {
		return MessageUtil.getMessage(target);
	}
	public String getMessage(String target, @Nullable String[] args) {
		return MessageUtil.getMessage(target, args);
	}

	public ResponseEntity<ApiErrorResponse> toResponse(BaseException ex) {
		ErrorMessage error = ex.error();
		return toResponse(error, null);
	}

	public ResponseEntity<ApiErrorResponse> toResponse(ErrorMessage error, String[] args) {
		String message = getMessage(error.resName(), args);
		return ApiErrorResponse.toResponse(error, message);
	}
}
