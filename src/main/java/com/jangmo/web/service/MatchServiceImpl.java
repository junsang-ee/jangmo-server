package com.jangmo.web.service;

import com.jangmo.web.constants.match.MatchStatus;
import com.jangmo.web.model.dto.response.MatchListResponse;
import com.jangmo.web.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;

    @Override
    public List<MatchListResponse> getUpcomingMatchList() {
        List<MatchStatus> matchStatusList = List.of(
                MatchStatus.CONFIRMED,
                MatchStatus.PENDING
        );
        return matchRepository.findAllByMatchAtAfterAndStatusIn(
                    LocalDate.now(),
                    matchStatusList
                ).stream()
                .map(MatchListResponse::of)
                .collect(Collectors.toList());
    }
}
