package com.jangmo.web.service;

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

import java.util.List;

public interface AuthService {
    MemberSignupResponse signupMember(MemberSignUpRequest signup);

    MercenaryRegistrationResponse registerMercenary(MercenaryRegistrationRequest request);

    List<CityListResponse> getCities();

    List<DistrictListResponse> getDistricts(Long cityId);

    String sendAuthCode(VerificationCodeSendRequest request);

    void verifyCode(VerificationCodeVerifyRequest request);

    String loginMember(String userAgent, MemberLoginRequest request);

    String loginMercenary(String userAgent, MercenaryLoginRequest request);

}
