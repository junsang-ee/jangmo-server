package com.jangmo.web.service;

import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.dto.request.MercenaryRegistrationRequest;
import com.jangmo.web.model.dto.response.CityListResponse;
import com.jangmo.web.model.dto.response.DistrictListResponse;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;

import java.util.List;

public interface AuthService {
    MemberEntity signupMember(MemberSignUpRequest signup);

    MercenaryEntity registerMercenary(MercenaryRegistrationRequest request);

    List<CityListResponse> getCities();

    List<DistrictListResponse> getDistrictsByCityId(Long cityId);

    List<DistrictListResponse> getDistrictsByCityName(String cityName);

}
