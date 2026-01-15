package com.jangmo.web.model.dto.request.board.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardCreateRequest {

	@NotBlank(message = "게시판 이름은 필수 항목입니다.")
	private String name;
}
