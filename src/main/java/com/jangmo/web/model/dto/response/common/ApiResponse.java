package com.jangmo.web.model.dto.response.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public abstract class ApiResponse {

	private final int code;

	private final String message;
}
