package com.jangmo.web.model.dto.request.board;

import com.jangmo.web.constants.ReplyTargetType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class ReplyCreateRequest {

    @NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
    private String content;

    @NotNull(message = "댓글을 등록할 항목의 타입은 필수 항목입니다.")
    private ReplyTargetType targetType;

    @NotNull(message = "댓글을 작성할 대상이 지정되지 않았습니다.")
    private String targetId;
}
