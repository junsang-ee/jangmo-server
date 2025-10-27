package com.jangmo.web.model.dto.request.board.manager;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardUpdateRequest {

    @NotBlank(message = "수정할 게시판 이름은 비어 있을 수 없습니다.")
    private String name;

}
