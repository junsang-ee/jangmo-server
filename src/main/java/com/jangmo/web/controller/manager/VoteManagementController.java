package com.jangmo.web.controller.manager;

import com.jangmo.web.model.dto.request.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.response.MatchVoteCreateResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.service.manager.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.wrap;

@RequiredArgsConstructor
@RequestMapping("/api/managers/votes")
@RestController
public class VoteManagementController {

    private final VoteService voteService;

    @PostMapping("/matches")
    public ResponseEntity<ApiSuccessResponse<MatchVoteCreateResponse>> createMatchVote(
            @AuthenticationPrincipal(expression = "id") String userId,
            @Valid @RequestBody MatchVoteCreateRequest request) {
        return wrap(voteService.createMatchVote(userId, request));
    }

    @DeleteMapping("/{voteId}")
    public ResponseEntity<ApiSuccessResponse<Object>> deleteVote(@PathVariable String voteId) {
        return wrap(null);
    }



}
