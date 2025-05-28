package com.jangmo.web.controller;

import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.dto.request.MercenaryRegistrationRequest;
import com.jangmo.web.model.dto.request.VerificationCodeSendRequest;
import com.jangmo.web.model.dto.request.VerificationCodeVerifyRequest;
import com.jangmo.web.model.dto.request.MemberLoginRequest;
import com.jangmo.web.model.dto.request.MercenaryLoginRequest;

import com.jangmo.web.model.dto.response.CityListResponse;
import com.jangmo.web.model.dto.response.DistrictListResponse;
import com.jangmo.web.model.dto.response.MemberSignupResponse;
import com.jangmo.web.model.dto.response.MercenaryRegistrationResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.model.dto.response.common.TokenResponse;
import com.jangmo.web.service.AuthService;

import com.jangmo.web.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.jangmo.web.model.dto.response.common.ApiSuccessResponse.wrap;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/signup/member")
    public ResponseEntity<ApiSuccessResponse<MemberSignupResponse>> signupMember(@Valid @RequestBody MemberSignUpRequest signup) {
        return wrap(authService.signupMember(signup));
    }

    @PostMapping("/register/mercenary")
    public ResponseEntity<ApiSuccessResponse<MercenaryRegistrationResponse>> registerMercenary(@RequestBody MercenaryRegistrationRequest registration) {
        return wrap(authService.registerMercenary(registration));
    }

    @PostMapping("/verification-codes")
    public ResponseEntity<ApiSuccessResponse<Object>> sendCode(@RequestBody VerificationCodeSendRequest request) {
        authService.sendAuthCode(request);
        return wrap(null);
    }

    @PostMapping("/verification-codes/verify")
    public ResponseEntity<ApiSuccessResponse<Object>> verifyCode(@RequestBody VerificationCodeVerifyRequest request) {
        authService.verifyCode(request);
        return wrap(null);
    }

    @GetMapping("/signup/cities")
    public ResponseEntity<ApiSuccessResponse<List<CityListResponse>>> cities() {
        return wrap(authService.getCities());
    }

    @GetMapping("/signup/cities/{cityId}/districts")
    public ResponseEntity<ApiSuccessResponse<List<DistrictListResponse>>> districts(@PathVariable Long cityId) {
        return wrap(authService.getDistricts(cityId));
    }

    @PostMapping("/login/member")
    public ResponseEntity<ApiSuccessResponse<TokenResponse>> loginMember(
            HttpServletRequest request,
            @Valid @RequestBody MemberLoginRequest login) {
        String userAgent = request.getHeader("User-Agent");
        return wrap(new TokenResponse(authService.loginMember(userAgent, login)));
    }

    @PostMapping("/login/mercenary")
    public ResponseEntity<ApiSuccessResponse<TokenResponse>> loginMercenary(
            HttpServletRequest request,
            @Valid @RequestBody MercenaryLoginRequest login) {
        String userAgent = request.getHeader("User-Agent");
        return wrap(new TokenResponse(authService.loginMercenary(userAgent, login)));
    }

    @PostMapping("/members/temp-password")
    public ResponseEntity<ApiSuccessResponse<Object>> resetPassword() {
        return null;
    }

}
