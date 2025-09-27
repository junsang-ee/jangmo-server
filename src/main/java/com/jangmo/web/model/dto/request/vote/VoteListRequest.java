package com.jangmo.web.model.dto.request.vote;

import com.jangmo.web.constants.vote.VoteType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoteListRequest {

    @NotNull(message = "년도는 필수 항목입니다.")
    private Integer year;

    @NotNull(message = "월은 필수 항목입니다.")
    @Min(value = 1, message = "월은 1 이상이어야 합니다.")
    @Max(value = 12, message = "월은 12 이하여야 합니다.")
    private Integer month;

    @NotNull(message = "투표 타입은 필수 항목입니다.")
    private VoteType voteType;

}
