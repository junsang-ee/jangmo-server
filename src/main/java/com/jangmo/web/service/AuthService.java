package com.jangmo.web.service;

import com.jangmo.web.model.dto.request.*;
import com.jangmo.web.model.dto.response.CityListResponse;
import com.jangmo.web.model.dto.response.DistrictListResponse;
import com.jangmo.web.model.dto.response.MemberSignupResponse;
import com.jangmo.web.model.dto.response.MercenaryRegistrationResponse;
import com.jangmo.web.model.entity.user.MercenaryEntity;

import java.util.List;

public interface AuthService {
    MemberSignupResponse signupMember(MemberSignUpRequest signup);

    MercenaryRegistrationResponse registerMercenary(MercenaryRegistrationRequest request);

    List<CityListResponse> getCities();

    List<DistrictListResponse> getDistricts(Long cityId);

    String sendAuthCode(MobileRequest request);

    void verifyCode(VerificationRequest request);

    String loginMember(String userAgent, MemberLoginRequest request);

    String loginMercenary(String userAgent, MercenaryLoginRequest request);

}
