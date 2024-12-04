package com.jangmo.web.service;

import com.jangmo.web.constants.cache.CacheType;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.custom.AuthException;
import com.jangmo.web.exception.custom.NotFoundException;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.dto.request.MercenaryRegistrationRequest;
import com.jangmo.web.model.dto.request.VerificationRequest;
import com.jangmo.web.model.dto.request.MobileRequest;
import com.jangmo.web.model.dto.response.CityListResponse;
import com.jangmo.web.model.dto.response.DistrictListResponse;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.repository.CityRepository;
import com.jangmo.web.repository.DistrictRepository;
import com.jangmo.web.repository.MemberRepository;

import com.jangmo.web.repository.MercenaryRepository;
import com.jangmo.web.service.cache.CacheService;
import com.jangmo.web.service.sms.SmsService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;

    private final MercenaryRepository mercenaryRepository;

    private final CityRepository cityRepository;

    private final DistrictRepository districtRepository;

    private final SmsService smsService;

    private final CacheService cacheService;

    @Override
    @Transactional
    public MemberEntity signupMember(MemberSignUpRequest signup) {
        MemberEntity member = MemberEntity.create(signup);
        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public MercenaryEntity registerMercenary(MercenaryRegistrationRequest request) {
        MercenaryEntity mercenary = MercenaryEntity.create(request);
        return mercenaryRepository.save(mercenary);
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

    @Override
    public String sendAuthCode(MobileRequest request) {
        String code = getRandomCode();
        smsService.send(request.getMobile(), code);
        return code;
    }

    @Override
    public void verifyCode(VerificationRequest request) {
        String code = null;
        try {
            code = cacheService.get(
                    CacheType.SIGNUP_CODE,
                    request.getMobile()
            );
        } catch (NullPointerException e) {
            throw new AuthException(ErrorMessage.AUTH_CODE_EXPIRED);
        }
        if (!code.equals(request.getCode()))
            throw new AuthException(ErrorMessage.AUTH_CODE_INVALID);
    }

    private String getRandomCode() {
        Random random = new Random();
        return String.valueOf(
                random.nextInt(888888) + 111111
        );
    }
}
