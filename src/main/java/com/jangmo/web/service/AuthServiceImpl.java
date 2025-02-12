package com.jangmo.web.service;

import com.jangmo.web.config.jwt.JwtTokenProvider;
import com.jangmo.web.config.sms.SmsProvider;
import com.jangmo.web.constants.MercenaryStatus;
import com.jangmo.web.constants.SmsType;
import com.jangmo.web.constants.cache.CacheType;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.custom.AuthException;
import com.jangmo.web.exception.custom.InvalidStateException;
import com.jangmo.web.exception.custom.NotFoundException;
import com.jangmo.web.model.dto.request.*;

import com.jangmo.web.model.dto.response.CityListResponse;
import com.jangmo.web.model.dto.response.DistrictListResponse;
import com.jangmo.web.model.dto.response.MemberSignupResponse;
import com.jangmo.web.model.dto.response.MercenaryRegistrationResponse;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.user.MercenaryTransientEntity;
import com.jangmo.web.repository.*;

import com.jangmo.web.service.cache.CacheService;

import com.jangmo.web.utils.EncryptUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;

    private final MercenaryRepository mercenaryRepository;

    private final CityRepository cityRepository;

    private final DistrictRepository districtRepository;

    private final MercenaryTransientRepository mercenaryTransientRepository;

    private final SmsProvider smsProvider;

    private final CacheService cacheService;

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    @Transactional
    public MemberSignupResponse signupMember(MemberSignUpRequest signup) {
        City city = getCity(signup.getCityId());
        District district = getDistrict(signup.getDistrictId());
        MemberEntity member = MemberEntity.create(
                signup,
                city,
                district
        );
        return MemberSignupResponse.of(
                memberRepository.save(member)
        );
    }

    @Override
    @Transactional
    public MercenaryRegistrationResponse registerMercenary(MercenaryRegistrationRequest request) {
        MercenaryEntity mercenary = MercenaryEntity.create(request);
        return MercenaryRegistrationResponse.of(
                mercenaryRepository.save(mercenary)
        );
    }

    @Override
    public List<CityListResponse> getCities() {
        return cityRepository.findAll().stream().map(
                CityListResponse::of
        ).collect(Collectors.toList());
    }

    @Override
    public List<DistrictListResponse> getDistricts(Long cityId) {
        City city = cityRepository.findById(cityId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.CITY_NOT_FOUND)
        );
        return districtRepository.findByCity(city).stream().map(
                DistrictListResponse::of
        ).collect(Collectors.toList());
    }

    @Override
    public String sendAuthCode(MobileRequest request) {
        String code = getRandomCode();
        smsProvider.send(request.getMobile(), code, SmsType.AUTH);
        return code;
    }

    @Override
    public void verifyCode(VerificationRequest request) {
        String code = null;
        try {
            code = cacheService.get(CacheType.SIGNUP_CODE, request.getMobile());
        } catch (NullPointerException e) {
            throw new AuthException(ErrorMessage.AUTH_CODE_EXPIRED);
        }
        if (!code.equals(request.getCode()))
            throw new AuthException(ErrorMessage.AUTH_CODE_INVALID);
    }

    @Override
    public String loginMember(String userAgent, MemberLoginRequest request) {
        MemberEntity member = memberRepository.findByMobile(request.getMobile()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND)
        );
        validMember(member, request.getPassword());
        return jwtTokenProvider.create(userAgent, member.getId(), member.getRole());
    }

    @Override
    public String loginMercenary(String userAgent, MercenaryLoginRequest request) {
        MercenaryEntity mercenary = mercenaryRepository.findByMobile(request.getMobile()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MERCENARY_NOT_FOUND)
        );
        validMercenary(mercenary, request.getMercenaryCode());
        return jwtTokenProvider.create(userAgent, mercenary.getId(), mercenary.getRole());
    }

    private String getRandomCode() {
        Random random = new Random();
        return String.valueOf(
                random.nextInt(888888) + 111111
        );
    }

    private City getCity(Long cityId) {
        return cityRepository.findById(cityId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.CITY_NOT_FOUND)
        );
    }

    private District getDistrict(Long districtId) {
        return districtRepository.findById(districtId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.DISTRICT_NOT_FOUND)
        );
    }

    private void validMember(MemberEntity member, String password) {
        if (EncryptUtil.matches(password, member.getPassword())) {
            switch (member.getStatus()) {
                case DISABLED:
                    throw new AuthException(ErrorMessage.AUTH_DISABLED);
                case PENDING:
                    throw new AuthException(ErrorMessage.AUTH_UNAUTHENTICATED);
                case RETIRED:
                    throw new AuthException(ErrorMessage.AUTH_RETIRED);
                default: return;
            }
        }
        throw new InvalidStateException(ErrorMessage.AUTH_PASSWORD_INVALID);
    }

    private void validMercenary(MercenaryEntity mercenary, String code) {
        MercenaryTransientEntity transientEntity =
                mercenaryTransientRepository.findByMercenary(mercenary).orElseGet(() -> null);

        if (transientEntity == null) {
            switch (mercenary.getStatus()) {
                case PENDING:
                    throw new AuthException(ErrorMessage.AUTH_UNAUTHENTICATED);
                case DISABLED:
                    throw new AuthException(ErrorMessage.AUTH_DISABLED);
                case EXPIRED:
                    throw new AuthException(ErrorMessage.AUTH_MERCENARY_EXPIRED);
            }
        }

        if (Objects.requireNonNull(transientEntity).getCode() == null) {
            throw new NotFoundException(ErrorMessage.MERCENARY_CODE_NOT_FOUND);
        }
        if (!EncryptUtil.matches(code, transientEntity.getCode())) {
            throw new AuthException(ErrorMessage.AUTH_MERCENARY_CODE_INVALID);
        }
    }

}
