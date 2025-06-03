package com.jangmo.web.controller;

import com.jangmo.web.model.dto.request.*;

import com.jangmo.web.model.dto.response.MemberSignupResponse;
import com.jangmo.web.model.dto.response.MercenaryRegistrationResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.model.dto.response.common.TokenResponse;
import com.jangmo.web.service.AuthService;

import com.jangmo.web.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/members/password")
    public ResponseEntity<ApiSuccessResponse<Object>> resetPassword(@RequestBody ResetPasswordRequest reset) {
        authService.resetMemberPassword(reset);
        return wrap(null);
    }

    @PatchMapping("/mercenaries/code")
    public ResponseEntity<ApiSuccessResponse<Object>> resetMercenaryCode(@RequestBody ResetMercenaryCodeRequest reset) {
        authService.resetMercenaryCode(reset);
        return wrap(null);
    }

}
