package com.jangmo.web.model.dto.request.board.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateRequest {
	private String title;
	private String content;
}
