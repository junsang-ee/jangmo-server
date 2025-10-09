package com.jangmo.web.model.dto.request.vote;

import com.jangmo.web.constants.vote.VoteModeType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class GeneralVoteCreateRequest {

    @NotBlank(message = "투표 제목은 필수 항목입니다.")
    private String title;

    @NotNull(message = "투표 마감 날짜는 필수 항목입니다.")
    private LocalDate endAt;

    @NotEmpty(message = "투표 옵션은 필수 항목입니다.")
    private List<@NotBlank(message = "투표 옵션 값은 비어 있을 수 없습니다.") String> options;

    @NotNull(message = "중복 투표 가능 여부는 필수 항목입니다.")
    private VoteModeType modeType;
}
