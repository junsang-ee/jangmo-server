package com.jangmo.web.service;

import com.jangmo.web.model.dto.request.MemberSignupRequest;
import com.jangmo.web.model.dto.request.MercenaryRegistrationRequest;
import com.jangmo.web.model.dto.response.CityListResponse;
import com.jangmo.web.model.dto.response.DistrictListResponse;
import com.jangmo.web.model.entity.MemberEntity;
import com.jangmo.web.model.entity.MercenaryEntity;

import java.util.List;

public interface AuthService {
    MemberEntity signupMember(MemberSignupRequest signup);

    MercenaryEntity registerMercenary(MercenaryRegistrationRequest request);

    List<CityListResponse> getCities();

    List<DistrictListResponse> getDistrictsByCityId(Long cityId);

    List<DistrictListResponse> getDistrictsByCityName(String cityName);

}
