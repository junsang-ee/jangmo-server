package com.jangmo.web.controller;

import com.jangmo.web.model.dto.request.BackNumberUpdateRequest;
import com.jangmo.web.model.dto.request.MemberAddressUpdateRequest;
import com.jangmo.web.model.dto.request.MemberPasswordUpdateRequest;
import com.jangmo.web.model.dto.request.UniformRegistrationRequest;
import com.jangmo.web.model.dto.response.MemberDetailResponse;
import com.jangmo.web.model.dto.response.UserDetailResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

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
            @Valid @RequestBody MemberPasswordUpdateRequest request) {
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
            @Valid @RequestBody MemberAddressUpdateRequest address) {
        userService.updateAddress(memberId, address.getCityId(), address.getDistrictId());
        return wrap(null);
    }

    @PostMapping("/members/uniform")
    public ResponseEntity<ApiSuccessResponse<Object>> registerUniform(
            @AuthenticationPrincipal(expression = "id") String memberId,
            @Valid @RequestBody UniformRegistrationRequest request) {
        userService.registerUniform(memberId, request.getBackNumber());
        return wrap(null);
    }

    @PatchMapping("/members/uniform")
    public ResponseEntity<ApiSuccessResponse<Object>> updateBackNumber(
            @AuthenticationPrincipal(expression = "id") String memberId,
            @Valid @RequestBody BackNumberUpdateRequest request) {
        userService.updateBackNumber(memberId, request.getBackNumber());
        return wrap(null);
    }

    @DeleteMapping("/members/retire")
    public ResponseEntity<ApiSuccessResponse<Object>> retireMember(
            @AuthenticationPrincipal(expression = "id") String memberId) {
        userService.retireMember(memberId);
        return wrap(null);
    }

}
