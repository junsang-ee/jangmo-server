package com.jangmo.web.controller;

import com.jangmo.web.controller.base.BaseController;
import com.jangmo.web.model.dto.request.MemberUpdatePasswordRequest;
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

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController extends BaseController {
    private final UserService userService;

    @GetMapping("/me")
    public ApiSuccessResponse<UserDetailResponse> me(@AuthenticationPrincipal(expression = "id") String userId) {
        return wrap(userService.getDetail(userId));
    }

    @PatchMapping("/member/{memberId}/password")
    public ApiSuccessResponse<Object> updatePassword(@AuthenticationPrincipal(expression = "id") String memberId,
                                                     @RequestBody MemberUpdatePasswordRequest request) {
        userService.updatePassword(memberId, request.getOldPassword(), request.getNewPassword());
        return wrap(null);
    }


}
