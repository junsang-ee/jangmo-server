package com.jangmo.web.service;

import com.jangmo.web.model.dto.request.MemberSignupRequest;
import com.jangmo.web.model.dto.response.CityListResponse;
import com.jangmo.web.model.dto.response.DistrictListResponse;
import com.jangmo.web.model.entity.MemberEntity;

import java.util.List;

public interface AuthService {
    MemberEntity signUp(MemberSignupRequest signup);

    List<CityListResponse> getCities();

    List<DistrictListResponse> getDistrictsByCityId(Long cityId);

    List<DistrictListResponse> getDistrictsByCityName(String cityName);

}
