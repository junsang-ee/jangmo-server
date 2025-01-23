package com.jangmo.web.controller;

import com.jangmo.web.controller.base.BaseController;
import com.jangmo.web.model.dto.response.UserDetailResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiSuccessResponse<Object> updatePassword() {
        return wrap(null);
    }


}
