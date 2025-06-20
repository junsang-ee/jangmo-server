package com.jangmo.web.model.dto.request;

import com.jangmo.web.constants.match.MatchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MatchVoteCreateRequest {

    @NotNull(message = "매치 타입은 필수 항목입니다.")
    private MatchType matchType;

    @NotNull(message = "매치 날짜는 필수 항목입니다.")
    private LocalDate matchAt;

    @NotNull(message = "투표의 마감 날짜는 필수 항목입니다.")
    private LocalDate endAt;
}
