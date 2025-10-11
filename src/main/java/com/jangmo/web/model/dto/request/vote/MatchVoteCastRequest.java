package com.jangmo.web.model.dto.request.vote;

import com.jangmo.web.config.validator.ValidFields;
import com.jangmo.web.constants.vote.MatchVoteOption;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MatchVoteCastRequest {

    @NotNull(message = "투표 옵션은 필수 항목입니다.")
    @ValidFields(field = "matchVoteOption")
    private MatchVoteOption option;

}
