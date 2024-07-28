package com.jangmo.web.model.dto.response.common;

import lombok.Getter;

@Getter
public class ApiErrorResponse extends ApiResponse {
    public ApiErrorResponse(int code, String message) {
        super(code, message);
    }
}
