package com.jangmo.web.service;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.custom.NotFoundException;
import com.jangmo.web.model.dto.request.MemberSignupRequest;
import com.jangmo.web.model.dto.response.CityListResponse;
import com.jangmo.web.model.dto.response.DistrictListResponse;
import com.jangmo.web.model.entity.MemberEntity;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.repository.CityRepository;
import com.jangmo.web.repository.DistrictRepository;
import com.jangmo.web.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;

    private final CityRepository cityRepository;

    private final DistrictRepository districtRepository;

    @Override
    @Transactional
    public MemberEntity signUp(MemberSignupRequest signup) {
        MemberEntity member = MemberEntity.create(signup);
        return memberRepository.save(member);
    }

    @Override
    public List<CityListResponse> getCities() {
        return cityRepository.findAll().stream().map(
                CityListResponse::of
        ).collect(Collectors.toList());
    }

    @Override
    public List<DistrictListResponse> getDistrictsByCityId(Long cityId) {
        City city = cityRepository.findById(cityId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.CITY_NOT_FOUND)
        );
        return districtRepository.findByCity(city).stream().map(
                DistrictListResponse::of
        ).collect(Collectors.toList());
    }

    @Override
    public List<DistrictListResponse> getDistrictsByCityName(String cityName) {
        City city = cityRepository.findByName(cityName).orElseThrow(
                () -> new NotFoundException(ErrorMessage.CITY_NOT_FOUND)
        );
        return districtRepository.findByCity(city).stream().map(
                DistrictListResponse::of
        ).collect(Collectors.toList());
    }

}
