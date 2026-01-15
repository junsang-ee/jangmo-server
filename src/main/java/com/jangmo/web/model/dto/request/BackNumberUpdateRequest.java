package com.jangmo.web.model.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class BackNumberUpdateRequest {
	@NotNull(message = "등번호는 필수 항목입니다.")
	private Integer backNumber;
}
