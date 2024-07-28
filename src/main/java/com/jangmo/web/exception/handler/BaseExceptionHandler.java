package com.jangmo.web.exception.handler;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.custom.BaseException;
import com.jangmo.web.model.dto.response.common.ApiErrorResponse;
import com.jangmo.web.utils.MessageUtil;
import org.springframework.lang.Nullable;

public class BaseExceptionHandler {
    public String getMessage(String target, @Nullable String[] args) {
        return MessageUtil.getMessage(target, args);
    }

    public ApiErrorResponse toResponse(BaseException ex) {
        ErrorMessage error = ex.error();
        return toResponse(error, null);
    }

    public ApiErrorResponse toResponse(ErrorMessage error, String[] args) {
        String message = getMessage(error.resName(), args);
        return new ApiErrorResponse(error.getCode(), message);
    }
}
