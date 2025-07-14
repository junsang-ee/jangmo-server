package com.jangmo.web.controller.manager;

import com.jangmo.web.model.dto.request.vote.GeneralVoteCreateRequest;
import com.jangmo.web.model.dto.request.vote.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.response.vote.MatchVoteCreateResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.service.manager.VoteManagementService;
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

    private final VoteManagementService voteManagementService;

    @PostMapping("/matches")
    public ResponseEntity<ApiSuccessResponse<MatchVoteCreateResponse>> createMatchVote(
            @AuthenticationPrincipal(expression = "id") String userId,
            @Valid @RequestBody MatchVoteCreateRequest request) {
        return wrap(voteManagementService.createMatchVote(userId, request));
    }

    @PostMapping("/general")
    public ResponseEntity<ApiSuccessResponse<Object>> createGeneralVote(
            @AuthenticationPrincipal(expression = "id") String userId,
            @Valid @RequestBody GeneralVoteCreateRequest request) {
        return wrap(voteManagementService.createGeneralVote(userId, request));
    }


    @GetMapping
    public ResponseEntity<ApiSuccessResponse<Object>> getVoteAll() {
        return wrap(null);
    }

    @DeleteMapping("/{voteId}")
    public ResponseEntity<ApiSuccessResponse<Object>> deleteVote(@PathVariable String voteId) {
        return wrap(null);
    }



}
