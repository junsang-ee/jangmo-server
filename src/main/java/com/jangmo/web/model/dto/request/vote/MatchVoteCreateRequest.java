package com.jangmo.web.model.dto.request.vote;

import com.jangmo.web.constants.match.MatchType;
import com.jangmo.web.constants.vote.VoteModeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MatchVoteCreateRequest {

    @NotBlank(message = "투표 제목은 필수 항목입니다.")
    private String title;

    @NotNull(message = "매치 타입은 필수 항목입니다.")
    private MatchType matchType;

    @NotNull(message = "매치 날짜는 필수 항목입니다.")
    private LocalDate matchAt;

    @NotNull(message = "투표 마감 날짜는 필수 항목입니다.")
    private LocalDate endAt;

    @NotNull(message = "중복 투표 가능 여부는 필수 항목입니다.")
    private VoteModeType modeType;
}
