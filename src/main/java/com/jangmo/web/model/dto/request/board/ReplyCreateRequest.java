package com.jangmo.web.model.dto.request.board;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class ReplyCreateRequest {

	@NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
	private String content;
}
