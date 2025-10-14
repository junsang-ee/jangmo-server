package com.jangmo.web.controller;


import com.jangmo.web.model.dto.request.vote.MatchVoteCastRequest;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.model.dto.response.vote.MatchVoteCastResponse;
import com.jangmo.web.model.dto.response.vote.UserMatchVoteStatusResponse;
import com.jangmo.web.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.wrap;

@RequiredArgsConstructor
@RequestMapping("/api/votes")
@RestController
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/matches/{matchVoteId}")
    public ResponseEntity<ApiSuccessResponse<MatchVoteCastResponse>> castMatchVote(
            @PathVariable String matchVoteId,
            @AuthenticationPrincipal(expression = "id") String userId,
            @RequestBody @Valid MatchVoteCastRequest request) {
        return wrap(voteService.castMatchVote(matchVoteId, userId, request));
    }

    @GetMapping("/matches/{matchVoteId}")
    public ResponseEntity<ApiSuccessResponse<UserMatchVoteStatusResponse>> getMatchVoteStatus(
            @PathVariable String matchVoteId,
            @AuthenticationPrincipal(expression = "id") String userId) {
        return wrap(voteService.getMatchVoteStatus(matchVoteId, userId));
    }
}
