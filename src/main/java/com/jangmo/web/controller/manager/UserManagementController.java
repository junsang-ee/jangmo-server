package com.jangmo.web.controller.manager;

import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.user.MercenaryStatus;
import com.jangmo.web.model.dto.request.MemberRoleUpdateRequest;
import com.jangmo.web.model.dto.request.MercenaryMatchRequest;
import com.jangmo.web.model.dto.request.UserListSearchRequest;
import com.jangmo.web.model.dto.response.MemberDetailResponse;
import com.jangmo.web.model.dto.response.MercenaryDetailResponse;
import com.jangmo.web.model.dto.response.UserListResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.model.dto.response.common.PageResponse;
import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.service.manager.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.page;
import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.wrap;

@RequiredArgsConstructor
@RequestMapping("/api/managers")
@RestController
public class UserManagementController {

    private final UserManagementService userManagementService;

    @GetMapping("/users")
    public ResponseEntity<ApiSuccessResponse<PageResponse<UserListResponse>>> getUserList(
            @AuthenticationPrincipal(expression = "id") String myId,
            @ParameterObject UserListSearchRequest request,
            Pageable pageable) {
        return page(userManagementService.getUserList(myId, request, pageable));
    }


    @GetMapping("/members/{memberId}")
    public ResponseEntity<ApiSuccessResponse<MemberDetailResponse>> getMemberDetail(@PathVariable String memberId) {
        return wrap(userManagementService.getMemberDetail(memberId));
    }

    @GetMapping("/mercenaries/{mercenaryId}")
    public ResponseEntity<ApiSuccessResponse<MercenaryDetailResponse>> getMercenaryDetail(@PathVariable String mercenaryId) {
        return wrap(userManagementService.getMercenaryDetail(mercenaryId));
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
        userManagementService.updateMemberStatus(memberId, status);
        return wrap(null);
    }

    @PatchMapping("/mercenaries/{mercenaryId}/status/{status}")
    public ResponseEntity<ApiSuccessResponse<Object>> updateMercenaryStatus(
            @PathVariable String mercenaryId,
            @PathVariable MercenaryStatus status) {
        userManagementService.updateMercenaryStatus(mercenaryId, status);
        return wrap(null);
    }

    @PatchMapping("/members/{memberId}/role")
    public ResponseEntity<ApiSuccessResponse<Object>> updateMemberRole(
            @PathVariable String memberId,
            @RequestBody MemberRoleUpdateRequest request) {
        userManagementService.updateMemberRole(memberId, request.getRole());
        return wrap(null);
    }

    @GetMapping("/users/pending-approval")
    public ResponseEntity<ApiSuccessResponse<List<UserEntity>>> getApprovalUsers() {
        return wrap(userManagementService.getApprovalUsers());
    }


}
