package com.jangmo.web.service;

import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.dto.request.MercenaryRegistrationRequest;
import com.jangmo.web.model.dto.request.MobileRequest;
import com.jangmo.web.model.dto.request.VerificationRequest;
import com.jangmo.web.model.dto.request.MemberLoginRequest;
import com.jangmo.web.model.dto.response.CityListResponse;
import com.jangmo.web.model.dto.response.DistrictListResponse;
import com.jangmo.web.model.dto.response.MemberSignupResponse;
import com.jangmo.web.model.entity.user.MercenaryEntity;

import java.util.List;

public interface AuthService {
    MemberSignupResponse signupMember(MemberSignUpRequest signup);

    MercenaryEntity registerMercenary(MercenaryRegistrationRequest request);

    List<CityListResponse> getCities();

    List<DistrictListResponse> getDistricts(Long cityId);

    String sendAuthCode(MobileRequest request);

    void verifyCode(VerificationRequest request);

    String loginMember(String userAgent, MemberLoginRequest request);

}
