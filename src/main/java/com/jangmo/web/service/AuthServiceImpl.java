package com.jangmo.web.service;

import com.jangmo.web.config.jwt.JwtTokenProvider;

import com.jangmo.web.constants.AuthPurposeType;
import com.jangmo.web.constants.SmsType;
import com.jangmo.web.constants.cache.CacheType;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.AuthException;
import com.jangmo.web.exception.InvalidStateException;
import com.jangmo.web.exception.NotFoundException;

import com.jangmo.web.infra.cache.CacheAccessor;
import com.jangmo.web.infra.sms.SmsProvider;
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
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.user.MercenaryTransientEntity;

import com.jangmo.web.repository.MemberRepository;
import com.jangmo.web.repository.MercenaryRepository;
import com.jangmo.web.repository.CityRepository;
import com.jangmo.web.repository.DistrictRepository;


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

    private final SmsProvider smsProvider;

    private final CacheAccessor cacheAccessor;

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
    public String sendAuthCode(VerificationCodeSendRequest request) {
        AuthPurposeType purposeType = request.getAuthPurposeType();
        String mobile = request.getMobile();
        if (purposeType == AuthPurposeType.RESET_PASSWORD)
            getMemberByMobile(mobile);
        else if (purposeType == AuthPurposeType.RESET_MERCENARY_CODE)
            getMercenaryByMobile(mobile);
        String code = getRandomCode();
        smsProvider.send(mobile, code, SmsType.AUTH_CODE);
        return code;
    }

    @Override
    public void verifyCode(VerificationCodeVerifyRequest request) {
        CacheType cacheType = request.getAuthPurposeType() == AuthPurposeType.SIGNUP ?
                CacheType.SIGNUP_CODE : CacheType.RESET_CODE;
        String code = cacheAccessor.get(cacheType, request.getMobile(), String.class).orElseThrow(
                () -> new AuthException(ErrorMessage.AUTH_CODE_EXPIRED)
        );
        if (!code.equals(request.getCode()))
            throw new AuthException(ErrorMessage.AUTH_CODE_INVALID);
    }

    @Override
    public String loginMember(String userAgent, MemberLoginRequest request) {
        MemberEntity member = memberRepository.findByMobile(request.getMobile()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND)
        );
        validateMember(member, request.getPassword());
        return jwtTokenProvider.create(userAgent, member.getId(), member.getRole());
    }

    @Override
    public String loginMercenary(String userAgent, MercenaryLoginRequest request) {
        MercenaryEntity mercenary = mercenaryRepository.findByMobile(request.getMobile()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MERCENARY_NOT_FOUND)
        );
        validateMercenary(mercenary, request.getMercenaryCode());
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

    private void validateMember(MemberEntity member, String password) {
        if (EncryptUtil.matches(password, member.getPassword())) {
            switch (member.getStatus()) {
                case DISABLED:
                    throw new AuthException(ErrorMessage.AUTH_DISABLED);
                case PENDING:
                    throw new AuthException(ErrorMessage.AUTH_UNAUTHENTICATED);
                default: return;
            }
        }
        throw new InvalidStateException(ErrorMessage.AUTH_PASSWORD_INVALID);
    }

    private void validateMercenary(MercenaryEntity mercenary, String code) {
        MercenaryTransientEntity transientEntity = mercenary.getMercenaryTransient();

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

    private void validateUserExists(String mobile, AuthPurposeType purposeType) {
        switch (purposeType) {
            case RESET_PASSWORD:
                getMemberByMobile(mobile);
                break;
            case RESET_MERCENARY_CODE:
                getMercenaryByMobile(mobile);
                break;
            default:
        }
    }

    private MemberEntity getMemberByMobile(String mobile) {
        return memberRepository.findByMobile(mobile).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND)
        );
    }

    private MercenaryEntity getMercenaryByMobile(String mobile) {
        return mercenaryRepository.findByMobile(mobile).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MERCENARY_NOT_FOUND)
        );
    }

}
