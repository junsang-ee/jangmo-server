package com.jangmo.web.model.dto.response.common;

import com.jangmo.web.constants.message.ErrorMessage;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class ApiErrorResponse extends ApiResponse {
    public ApiErrorResponse(int code, String message) {
        super(code, message);
    }

    public static ResponseEntity<ApiErrorResponse> toResponse(ErrorMessage error, String message) {

        return ResponseEntity.status(error.status())
                .body(
                        new ApiErrorResponse(error.code(), message)
                );
    }
}
