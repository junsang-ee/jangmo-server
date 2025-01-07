package com.jangmo.web.controller;

import com.jangmo.web.controller.base.BaseController;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.dto.request.MercenaryRegistrationRequest;
import com.jangmo.web.model.dto.request.MemberLoginRequest;
import com.jangmo.web.model.dto.request.MobileRequest;
import com.jangmo.web.model.dto.request.VerificationRequest;
import com.jangmo.web.model.dto.response.CityListResponse;
import com.jangmo.web.model.dto.response.DistrictListResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.model.dto.response.common.TokenResponse;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.service.AuthService;

import com.jangmo.web.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController extends BaseController {

    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/signup/member")
    public ApiSuccessResponse<MemberEntity> signupMember(@Valid @RequestBody MemberSignUpRequest signup) {
        return wrap(authService.signupMember(signup));
    }

    @PostMapping("/register/mercenary")
    public ApiSuccessResponse<MercenaryEntity> registerMercenary(@RequestBody MercenaryRegistrationRequest registration) {
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

    @GetMapping("/signup/cities/{cityId}/districts")
    public ApiSuccessResponse<List<DistrictListResponse>> districts(@PathVariable Long cityId) {
        return wrap(authService.getDistricts(cityId));
    }

    @PostMapping("/login/member")
    public ApiSuccessResponse<TokenResponse> loginMember(HttpServletRequest request,
                                                         @Valid @RequestBody MemberLoginRequest loginRequest) {
        String userAgent = request.getHeader("User-Agent");
        return wrap(new TokenResponse(authService.loginMember(userAgent, loginRequest)));
    }

}
