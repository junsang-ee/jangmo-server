package com.jangmo.web.model.dto.request.board;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class PostCreateRequest {

    @NotBlank(message = "게시글 제목은 필수 항목입니다.")
    private String title;

    @NotBlank(message = "게시글 내용은 필수 항목입니다.")
    private String content;

}
