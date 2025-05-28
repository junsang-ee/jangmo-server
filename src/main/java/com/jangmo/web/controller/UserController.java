package com.jangmo.web.controller;

import com.jangmo.web.model.dto.request.MemberUpdateAddressRequest;
import com.jangmo.web.model.dto.request.MemberUpdatePasswordRequest;
import com.jangmo.web.model.dto.response.MemberDetailResponse;
import com.jangmo.web.model.dto.response.UserDetailResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.wrap;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiSuccessResponse<UserDetailResponse>> me(
            @AuthenticationPrincipal(expression = "id") String userId) {
        return wrap(userService.getDetail(userId));
    }

    @PatchMapping("/members/password")
    public ResponseEntity<ApiSuccessResponse<Object>> updatePassword(
            @AuthenticationPrincipal(expression = "id") String memberId,
            @Valid @RequestBody MemberUpdatePasswordRequest request) {
        userService.updatePassword(memberId, request.getOldPassword(), request.getNewPassword());
        return wrap(null);
    }

    @GetMapping("/members/me")
    public ResponseEntity<ApiSuccessResponse<MemberDetailResponse>> getMemberDetail(
            @AuthenticationPrincipal(expression = "id") String memberId) {
        return wrap(userService.getMemberDetail(memberId));
    }

    @PatchMapping("/members/address")
    public ResponseEntity<ApiSuccessResponse<Object>> updateAddress(
            @AuthenticationPrincipal(expression = "id") String memberId,
            @Valid @RequestBody MemberUpdateAddressRequest address) {
        userService.updateAddress(memberId, address.getCityId(), address.getDistrictId());
        return wrap(null);
    }

    @DeleteMapping("/members/retire")
    public ResponseEntity<ApiSuccessResponse<Object>> retireMember(
            @AuthenticationPrincipal(expression = "id") String memberId) {
        userService.retireMember(memberId);
        return wrap(null);
    }

}
