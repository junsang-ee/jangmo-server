package com.jangmo.web.controller.manager;

import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.user.MercenaryStatus;
import com.jangmo.web.model.dto.request.GroundCreateRequest;
import com.jangmo.web.model.dto.request.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.request.MercenaryMatchRequest;
import com.jangmo.web.model.dto.request.UserListSearchRequest;

import com.jangmo.web.model.dto.response.MatchVoteCreateResponse;
import com.jangmo.web.model.dto.response.MemberDetailResponse;
import com.jangmo.web.model.dto.response.MercenaryDetailResponse;
import com.jangmo.web.model.dto.response.UserListResponse;
import com.jangmo.web.model.dto.response.SearchPlaceResponse;
import com.jangmo.web.model.dto.response.GroundCreateResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;

import com.jangmo.web.model.dto.response.common.PageResponse;
import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.service.manager.GroundManagementService;
import com.jangmo.web.service.manager.UserManagementService;
import com.jangmo.web.service.manager.VoteService;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.wrap;
import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.page;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/managers")
@RestController
public class ManagerController {

    private final VoteService voteService;

    private final UserManagementService userManagementService;

    private final GroundManagementService groundManagementService;
    @GetMapping("/members/{memberId}")
    public ResponseEntity<ApiSuccessResponse<MemberDetailResponse>> getMemberDetail(@PathVariable String memberId) {
        return wrap(userManagementService.getMemberDetail(memberId));
    }

    @GetMapping("/mercenaries/{mercenaryId}")
    public ResponseEntity<ApiSuccessResponse<MercenaryDetailResponse>> getMercenaryDetail(@PathVariable String mercenaryId) {
        return wrap(userManagementService.getMercenaryDetail(mercenaryId));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiSuccessResponse<PageResponse<UserListResponse>>> getUserList(
            @AuthenticationPrincipal(expression = "id") String myId,
            @ParameterObject UserListSearchRequest request,
            Pageable pageable) {
        return page(userManagementService.getUserList(myId, request, pageable));
    }

    @PatchMapping("/mercenaries/{mercenaryId}/approve")
    public ResponseEntity<ApiSuccessResponse<Object>> approveMercenary(
            @PathVariable String mercenaryId,
            @RequestBody MercenaryMatchRequest matchRequest) {
        userManagementService.approveMercenary(mercenaryId, matchRequest.getMatchId());
        return wrap(null);
    }

    @PatchMapping("/members/{memberId}/approve")
    public ResponseEntity<ApiSuccessResponse<Object>> approveMember(@PathVariable String memberId) {
        userManagementService.approveMember(memberId);
        return wrap(null);
    }

    @PatchMapping("/members/{memberId}/status/{status}")
    public ResponseEntity<ApiSuccessResponse<Object>> updateMemberStatus(
            @PathVariable String memberId,
            @PathVariable MemberStatus status) {
        return wrap(null);
    }

    @PatchMapping("/mercenaries/{mercenaryId}/status/{status}")
    public ResponseEntity<ApiSuccessResponse<Object>> updateMercenaryStatus(
            @PathVariable String mercenaryId,
            @PathVariable MercenaryStatus status) {
        return wrap(null);
    }

    @PostMapping("/votes/matches")
    public ResponseEntity<ApiSuccessResponse<MatchVoteCreateResponse>> createMatchVote(
            @AuthenticationPrincipal(expression = "id") String userId,
            @Valid @RequestBody MatchVoteCreateRequest request) {
        return wrap(voteService.createMatchVote(userId, request));
    }

    @DeleteMapping("/votes/{voteId}")
    public ResponseEntity<ApiSuccessResponse<Object>> deleteVote(@PathVariable String voteId) {
        return wrap(null);
    }

    @GetMapping("/users/pending-approval")
    public ResponseEntity<ApiSuccessResponse<List<UserEntity>>> getApprovalUsers() {
        return wrap(userManagementService.getApprovalUsers());
    }

    @GetMapping("/ground/{keyword}")
    public ResponseEntity<ApiSuccessResponse<List<SearchPlaceResponse>>> searchGrounds(@PathVariable String keyword) {
        return wrap(groundManagementService.searchGrounds(keyword));
    }

    @PostMapping("/ground")
    public ResponseEntity<ApiSuccessResponse<GroundCreateResponse>> createGround(
            @AuthenticationPrincipal(expression = "id") String createdById,
            @RequestBody GroundCreateRequest request) {
        return wrap(groundManagementService.createGround(createdById, request));
    }


}
