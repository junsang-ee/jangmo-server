package com.jangmo.web.controller.manager;

import com.jangmo.web.model.dto.request.vote.GeneralVoteCreateRequest;
import com.jangmo.web.model.dto.request.vote.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.request.vote.VoteListRequest;
import com.jangmo.web.model.dto.response.vote.MatchVoteCreateResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.model.dto.response.vote.VoteListResponse;
import com.jangmo.web.service.manager.VoteManagementService;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

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
    public ResponseEntity<ApiSuccessResponse<List<VoteListResponse>>> getVotes(
            @Validated @ParameterObject VoteListRequest request) {
        return wrap(voteManagementService.getVotes(request));
    }

    @DeleteMapping("/{voteId}")
    public ResponseEntity<ApiSuccessResponse<Object>> deleteVote(@PathVariable String voteId) {
        return wrap(null);
    }



}
