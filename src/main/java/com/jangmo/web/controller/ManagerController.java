package com.jangmo.web.controller;

import com.jangmo.web.constants.MemberStatus;
import com.jangmo.web.constants.MercenaryStatus;
import com.jangmo.web.controller.base.BaseController;
import com.jangmo.web.model.dto.request.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.request.MercenaryMatchRequest;
import com.jangmo.web.model.dto.response.MatchVoteCreateResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;

import com.jangmo.web.service.manager.UserManagementService;
import com.jangmo.web.service.manager.VoteService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


@RequiredArgsConstructor
@RequestMapping("/api/manager")
@RestController
public class ManagerController extends BaseController {

    private final VoteService voteService;

    private final UserManagementService userManagementService;

    @PatchMapping("/mercenary/{mercenaryId}/approve")
    public ApiSuccessResponse<Object> approveMercenary(@PathVariable String mercenaryId,
                                                       @RequestBody MercenaryMatchRequest matchRequest) {
        userManagementService.approveMercenary(mercenaryId, matchRequest.getMatchId());
        return wrap(null);
    }

    @PatchMapping("/member/{memberId}/approve")
    public ApiSuccessResponse<Object> approveMember(@PathVariable String memberId) {
        userManagementService.approveMember(memberId);
        return wrap(null);
    }

    @PatchMapping("/member/{memberId}/status/{status}")
    public ApiSuccessResponse<Object> updateMemberStatus(@PathVariable String memberId,
                                                         @PathVariable MemberStatus status) {
        return wrap(null);
    }

    @PatchMapping("/mercenary/{mercenaryId}/status/{status}")
    public ApiSuccessResponse<Object> updateMercenaryStatus(@PathVariable String mercenaryId,
                                                            @PathVariable MercenaryStatus status) {
        return wrap(null);
    }

    @PostMapping("/vote/match")
    public ApiSuccessResponse<MatchVoteCreateResponse> createMatchVote(@AuthenticationPrincipal(expression = "id") String userId,
                                                                       @Valid @RequestBody MatchVoteCreateRequest request) {
        return wrap(voteService.createMatchVote(userId, request));
    }

    @DeleteMapping("/vote/{voteId}")
    public ApiSuccessResponse<Object> deleteVote(@PathVariable String voteId) {
        return wrap(null);
    }




}
