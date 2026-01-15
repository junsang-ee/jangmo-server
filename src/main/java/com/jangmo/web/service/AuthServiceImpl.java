package com.jangmo.web.service;

import com.jangmo.web.config.jwt.JwtTokenProvider;

import com.jangmo.web.constants.AuthPurposeType;
import com.jangmo.web.constants.MercenaryRetentionStatus;
import com.jangmo.web.constants.SmsType;
import com.jangmo.web.constants.cache.CacheType;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.constants.user.MercenaryStatus;
import com.jangmo.web.exception.AuthException;
import com.jangmo.web.exception.conflict.DuplicatedException;
import com.jangmo.web.exception.InvalidStateException;
import com.jangmo.web.exception.NotFoundException;

import com.jangmo.web.infra.cache.CacheAccessor;
import com.jangmo.web.infra.sms.SmsProvider;
import com.jangmo.web.model.dto.request.*;

import com.jangmo.web.model.dto.response.MemberSignupResponse;
import com.jangmo.web.model.dto.response.MercenaryRegistrationResponse;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.user.MercenaryTransientEntity;

import com.jangmo.web.repository.*;


import com.jangmo.web.repository.user.MemberRepository;
import com.jangmo.web.repository.user.MercenaryRepository;
import com.jangmo.web.repository.user.UserRepository;
import com.jangmo.web.utils.CodeGeneratorUtil;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;


@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
	private final MemberRepository memberRepository;
	private final MercenaryRepository mercenaryRepository;
	private final UserRepository userRepository;
	private final CityRepository cityRepository;
	private final DistrictRepository districtRepository;
	private final SmsProvider smsProvider;
	private final CacheAccessor cacheAccessor;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;


	@Override
	@Transactional
	public MemberSignupResponse signupMember(MemberSignUpRequest signup) {
		validateVerifiedMobile(CacheType.SIGNUP_VERIFIED, signup.getMobile());
		City city = getCity(signup.getCityId());
		District district = getDistrict(signup.getDistrictId());

		String encodedPassword = passwordEncoder.encode(signup.getPassword());
		MemberEntity member = MemberEntity.create(
			signup.getName(),
			signup.getMobile(),
			signup.getGender(),
			signup.getBirth(),
			encodedPassword,
			city,
			district
		);
		return MemberSignupResponse.of(memberRepository.save(member));
	}

	@Override
	@Transactional
	public MercenaryRegistrationResponse registerMercenary(MercenaryRegistrationRequest registration) {
		validateVerifiedMobile(CacheType.SIGNUP_VERIFIED, registration.getMobile());
		MercenaryEntity mercenary = MercenaryEntity.create(registration);
		return MercenaryRegistrationResponse.of(
			mercenaryRepository.save(mercenary)
		);
	}

	@Override
	public void sendAuthCode(VerificationCodeSendRequest request) {
		AuthPurposeType purposeType = request.getAuthPurposeType();
		String mobile = request.getMobile();

		CacheType cacheType = CacheType.SIGNUP_CODE;
		if (purposeType == AuthPurposeType.SIGNUP) {
			checkDuplicatedUser(mobile);
		} else {
			checkResettableUser(mobile, purposeType);
			cacheType = CacheType.RESET_CODE;
		}
		String code = getRandomCode();
		putCacheAuthCode(cacheType, mobile, code);
		smsProvider.send(mobile, code, SmsType.AUTH_CODE);
	}

	private void checkDuplicatedUser(String mobile) {
		userRepository.findByMobile(mobile).ifPresent(
			val -> {
				throw new DuplicatedException(
					ErrorMessage.AUTH_USER_DUPLICATED
				);
			});
	}

	@Override
	public void verifyCode(VerificationCodeVerifyRequest request) {
		CacheType cacheType = request.getAuthPurposeType() == AuthPurposeType.SIGNUP ?
			CacheType.SIGNUP_CODE : CacheType.RESET_CODE;
		String mobile = request.getMobile();
		String cachedAuthCode = getCachedAuthCode(mobile, cacheType);
		if (!cachedAuthCode.equals(request.getCode()))
			throw new AuthException(ErrorMessage.AUTH_CODE_INVALID);
		putCacheVerifiedMobile(cacheType, mobile);
	}

	@Override
	public String loginMember(String userAgent, MemberLoginRequest login) {
		MemberEntity member = memberRepository.findByMobile(login.getMobile()).orElseThrow(
			() -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND)
		);
		validateMember(member, login.getPassword());
		return jwtTokenProvider.create(userAgent, member.getId(), member.getRole());
	}

	@Override
	public String loginMercenary(String userAgent, MercenaryLoginRequest login) {
		MercenaryEntity mercenary = mercenaryRepository.findByMobile(login.getMobile()).orElseThrow(
			() -> new NotFoundException(ErrorMessage.MERCENARY_NOT_FOUND)
		);
		validateMercenary(mercenary, login.getMercenaryCode());
		return jwtTokenProvider.create(userAgent, mercenary.getId(), mercenary.getRole());
	}

	@Override
	@Transactional
	public void resetMemberPassword(ResetPasswordRequest reset) {
		validateVerifiedMobile(CacheType.RESET_VERIFIED, reset.getMobile());
		MemberEntity member = getMemberByMobile(reset.getMobile());
		String encodedPassword = passwordEncoder.encode(reset.getNewPassword());
		member.updatePassword(encodedPassword);
	}

	@Override
	@Transactional
	public void resetMercenaryCode(ResetMercenaryCodeRequest reset) {
		String mobile = reset.getMobile();
		validateVerifiedMobile(CacheType.RESET_VERIFIED, mobile);
		MercenaryEntity mercenary = getMercenaryByMobile(mobile);
		String requestedCode = CodeGeneratorUtil.getMercenaryCode();
		String encodedCode = passwordEncoder.encode(requestedCode);
		MercenaryTransientEntity transientEntity = mercenary.getMercenaryTransient();

		transientEntity.updateCode(encodedCode);
		smsProvider.send(mobile, requestedCode, SmsType.MERCENARY_CODE);
	}

	private String getRandomCode() {
		Random random = new Random();
		return String.valueOf(random.nextInt(888888) + 111111);
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
		if (passwordEncoder.matches(password, member.getPassword())) {
			switch (member.getStatus()) {
				case DISABLED:
					throw new AuthException(ErrorMessage.AUTH_DISABLED);
				case PENDING:
					throw new AuthException(ErrorMessage.AUTH_PENDING);
				default: return;
			}
		}
		throw new InvalidStateException(ErrorMessage.AUTH_PASSWORD_INVALID);
	}

	private void validateMercenary(MercenaryEntity mercenary, String code) {
		MercenaryTransientEntity transientEntity = mercenary.getMercenaryTransient();
		MercenaryStatus status = mercenary.getStatus();
		if (status == MercenaryStatus.DISABLED) {
			throw new AuthException(ErrorMessage.AUTH_DISABLED);
		} else if (status == MercenaryStatus.EXPIRED) {
			throw new AuthException(ErrorMessage.AUTH_MERCENARY_EXPIRED);
		}
		if (transientEntity == null) {
			if (mercenary.getStatus() == MercenaryStatus.PENDING) {
				throw new AuthException(ErrorMessage.AUTH_PENDING);
			} else {
				log.error("MercenaryTransientEntity is Null Error >> {}, {}",
					mercenary.getMobile(), mercenary.getStatus());
			}
			throw new NotFoundException(ErrorMessage.AUTH_MERCENARY_CODE_NOT_ISSUED);
		}

		if (transientEntity.getCode() == null)
			throw new NotFoundException(ErrorMessage.AUTH_MERCENARY_CODE_NOT_ISSUED);

		if (!passwordEncoder.matches(code, transientEntity.getCode()))
			throw new AuthException(ErrorMessage.AUTH_MERCENARY_CODE_INVALID);
	}

	private void checkResettableUser(String mobile, AuthPurposeType authPurposeType) {
		if (authPurposeType == AuthPurposeType.RESET_PASSWORD) {
			getMemberByMobile(mobile);
		} else if (authPurposeType == AuthPurposeType.RESET_MERCENARY_CODE) {
			MercenaryEntity mercenary = getMercenaryByMobile(mobile);
			MercenaryTransientEntity mercenaryTransient = mercenary.getMercenaryTransient();
			if (mercenaryTransient == null || mercenaryTransient.getCode() == null) {
				if (mercenary.getRetentionStatus() == MercenaryRetentionStatus.KEEP)
					throw new AuthException(ErrorMessage.AUTH_MERCENARY_EXPIRED);
				throw new AuthException(ErrorMessage.AUTH_MERCENARY_CODE_NOT_ISSUED);
			}
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


	private void validateVerifiedMobile(CacheType cacheType, String mobile) {
		cacheAccessor.get(cacheType, mobile, Boolean.class)
			.filter(Boolean::booleanValue)
			.orElseThrow(
				() -> new AuthException(ErrorMessage.AUTH_NOT_VERIFIED)
			);
		cacheAccessor.remove(cacheType, mobile);
	}

	private void putCacheVerifiedMobile(CacheType cacheType, String mobile) {
		if (cacheType == CacheType.SIGNUP_CODE) {
			cacheAccessor.put(CacheType.SIGNUP_VERIFIED, mobile, true);
		} else if (cacheType == CacheType.RESET_CODE) {
			cacheAccessor.put(CacheType.RESET_VERIFIED, mobile, true);
		}
		cacheAccessor.remove(cacheType, mobile);
	}

	private String getCachedAuthCode(String mobile, CacheType cacheType) {
		return cacheAccessor.get(cacheType, mobile, String.class).orElseThrow(
			() -> new AuthException(ErrorMessage.AUTH_NOT_VERIFIED)
		);
	}

	private void putCacheAuthCode(CacheType cacheType, String mobile, String code) {
		cacheAccessor.get(cacheType, mobile, String.class).ifPresent(
			cachedAuthCode -> cacheAccessor.remove(cacheType, mobile)
		);
		cacheAccessor.put(cacheType, mobile, code);
	}

}
