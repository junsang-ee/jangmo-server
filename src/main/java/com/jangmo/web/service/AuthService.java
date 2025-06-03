package com.jangmo.web.service;

import com.jangmo.web.model.dto.request.*;

import com.jangmo.web.model.dto.response.CityListResponse;
import com.jangmo.web.model.dto.response.DistrictListResponse;
import com.jangmo.web.model.dto.response.MemberSignupResponse;
import com.jangmo.web.model.dto.response.MercenaryRegistrationResponse;

import java.util.List;

public interface AuthService {
    MemberSignupResponse signupMember(MemberSignUpRequest signup);

    MercenaryRegistrationResponse registerMercenary(MercenaryRegistrationRequest request);

    void sendAuthCode(VerificationCodeSendRequest request);

    void verifyCode(VerificationCodeVerifyRequest request);

    String loginMember(String userAgent, MemberLoginRequest login);

    String loginMercenary(String userAgent, MercenaryLoginRequest login);

    void resetMemberPassword(ResetPasswordRequest reset);

    void resetMercenaryCode(ResetMercenaryCodeRequest reset);

}
