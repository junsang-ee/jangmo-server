package com.jangmo.web.controller;

import com.jangmo.web.controller.base.BaseController;
import com.jangmo.web.model.dto.request.MemberUpdateAddressRequest;
import com.jangmo.web.model.dto.request.MemberUpdatePasswordRequest;
import com.jangmo.web.model.dto.response.MemberDetailResponse;
import com.jangmo.web.model.dto.response.UserDetailResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.service.UserService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController extends BaseController {
    private final UserService userService;

    @GetMapping("/me")
    public ApiSuccessResponse<UserDetailResponse> me(@AuthenticationPrincipal(expression = "id") String userId) {
        return wrap(userService.getDetail(userId));
    }

    @PatchMapping("/members/password")
    public ApiSuccessResponse<Object> updatePassword(@AuthenticationPrincipal(expression = "id") String memberId,
                                                     @Valid @RequestBody MemberUpdatePasswordRequest request) {
        userService.updatePassword(memberId, request.getOldPassword(), request.getNewPassword());
        return wrap(null);
    }

    @GetMapping("/members/me")
    public ApiSuccessResponse<MemberDetailResponse> getMemberDetail(@AuthenticationPrincipal(expression = "id") String memberId) {
        return wrap(userService.getMemberDetail(memberId));
    }

    @PatchMapping("/members/address")
    public ApiSuccessResponse<Object> updateAddress(@AuthenticationPrincipal(expression = "id") String memberId,
                                                    @Valid @RequestBody MemberUpdateAddressRequest address) {
        userService.updateAddress(memberId, address.getCityId(), address.getDistrictId());
        return wrap(null);
    }


}
