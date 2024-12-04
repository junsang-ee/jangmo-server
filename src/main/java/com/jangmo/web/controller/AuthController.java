package com.jangmo.web.controller;

import com.jangmo.web.controller.base.BaseController;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.dto.request.MercenaryRegistrationRequest;
import com.jangmo.web.model.dto.request.MobileRequest;
import com.jangmo.web.model.dto.request.VerificationRequest;
import com.jangmo.web.model.dto.response.CityListResponse;
import com.jangmo.web.model.dto.response.DistrictListResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.service.AuthService;

import com.jangmo.web.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController extends BaseController {

    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/signup/member")
    public ApiSuccessResponse<MemberEntity> signupMember(@RequestBody MemberSignUpRequest signup) {
        return wrap(authService.signupMember(signup));
    }

    @PostMapping("/signup/mercenary")
    public ApiSuccessResponse<MercenaryEntity> signupMercenary(@RequestBody MercenaryRegistrationRequest registration) {
        return wrap(authService.registerMercenary(registration));
    }

    @PostMapping("/signup/mobile/send-code")
    public ApiSuccessResponse<Object> sendCode(@RequestBody MobileRequest request) {
        authService.sendAuthCode(request);
        return wrap(null);
    }

    @PostMapping("/signup/mobile/verify-code")
    public ApiSuccessResponse<Objects> verifyCode(@RequestBody VerificationRequest request) {
        authService.verifyCode(request);
        return wrap(null);
    }


    @GetMapping("/signup/cities")
    public ApiSuccessResponse<List<CityListResponse>> cities() {
        return wrap(authService.getCities());
    }

    @GetMapping("/signup/cities/{cityName}/districts")
    public ApiSuccessResponse<List<DistrictListResponse>> districts(@PathVariable String cityName) {
        return wrap(authService.getDistrictsByCityName(cityName));
    }

}
