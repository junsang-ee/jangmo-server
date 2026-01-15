package com.jangmo.web.model.dto.request.board.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateRequest {

	@NotBlank(message = "게시글 제목은 필수 항목입니다.")
	private String title;

	@NotBlank(message = "게시글 내용은 필수 항목입니다.")
	private String content;

}
