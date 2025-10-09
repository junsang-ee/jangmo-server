package com.jangmo.web.service;

import com.jangmo.web.config.jwt.JwtConfig;
import com.jangmo.web.constants.AuthPurposeType;
import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.SmsType;
import com.jangmo.web.constants.cache.CacheType;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.MercenaryRetentionStatus;
import com.jangmo.web.constants.user.MercenaryStatus;
import com.jangmo.web.constants.match.MatchType;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.constants.vote.VoteModeType;
import com.jangmo.web.exception.AuthException;
import com.jangmo.web.exception.NotFoundException;

import com.jangmo.web.exception.conflict.DuplicatedException;
import com.jangmo.web.infra.cache.CacheAccessor;
import com.jangmo.web.infra.sms.SmsProvider;
import com.jangmo.web.model.dto.request.*;

import com.jangmo.web.model.dto.request.vote.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.response.MemberSignupResponse;

import com.jangmo.web.model.dto.response.MercenaryRegistrationResponse;
import com.jangmo.web.model.entity.MatchEntity;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.model.entity.user.MercenaryTransientEntity;

import com.jangmo.web.repository.*;

import com.jangmo.web.repository.user.MemberRepository;
import com.jangmo.web.repository.user.MercenaryRepository;
import com.jangmo.web.repository.vote.MatchVoteRepository;
import com.jangmo.web.service.manager.VoteManagementServiceImpl;
import com.jangmo.web.utils.AgeUtil;

import com.jangmo.web.utils.CodeGeneratorUtil;
import com.jangmo.web.utils.EncryptUtil;
import io.jsonwebtoken.Jwts;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@Slf4j
@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class AuthServiceTest {

    @Autowired MemberRepository memberRepository;
    @Autowired MercenaryRepository mercenaryRepository;
    @Autowired MatchVoteRepository matchVoteRepository;
    @Autowired MercenaryTransientRepository mercenaryTransientRepository;
    @Autowired VoteManagementServiceImpl voteService;
    @Autowired JwtConfig jwtConfig;
    @Autowired AuthServiceImpl authService;
    @Autowired CacheAccessor cacheAccessor;
    @Autowired CityRepository cityRepository;
    @Autowired DistrictRepository districtRepository;

    @Value("${jangmo.admin.name}")
    String adminName;

    @Value("${jangmo.admin.password}")
    String adminPassword;

    @Value("${jangmo.admin.mobile}")
    String adminMobile;

    static final String AUTH_CODE_SEND = "인증 코드 전송 및 휴대폰 번호 캐시 저장 결과 테스트";
    static final String AUTH_CODE_VERIFY = "인증 코드 검증 및 휴대폰 인증 여부 캐시 저장 테스트";
    static final String MEMBER_SIGNUP_SCENARIO = "회원(Member) 가입 시나리오 테스트";
    static final String MERCENARY_REGISTER_SCENARIO = "용병 등록 시나리오 테스트";
    static final String DUPLICATED_AUTH_CODE = "인증 번호 요청 중복 에러 테스트";
    static final String MEMBER_LOGIN = "회원(Member) 로그인 테스트";
    static final String MERCENARY_LOGIN = "용병(Mercenary) 로그인 테스트";
    static final String MEMBER_PASSWORD_RESET_SCENARIO = "회원(Member) 비밀번호 재설정 시나리오 테스트";
    static final String MERCENARY_CODE_RESET_SCENARIO = "용병(Mercenary) 코드 재발급 시나리오 테스트";

    @MockBean private SmsProvider smsProvider;

    MemberEntity admin;
    City city;
    District district;

    @BeforeEach
    void init(TestInfo testInfo) {
        String display = testInfo.getDisplayName();
        switch (display) {
            case MERCENARY_LOGIN:
            case MERCENARY_CODE_RESET_SCENARIO:
            case DUPLICATED_AUTH_CODE:
                initCityAndDistrict();
                createAdmin();
                break;
            case MEMBER_SIGNUP_SCENARIO:
            case MEMBER_LOGIN:
                initCityAndDistrict();
        }
    }

    void createAdmin() {
        MemberSignUpRequest adminSignup = new MemberSignUpRequest(
                adminName,
                adminMobile,
                Gender.MALE,
                LocalDate.of(1994, 3, 16),
                adminPassword,
                1L, 1L
        );

        admin = memberRepository.save(
                MemberEntity.create(
                        adminSignup, city, district
                )
        );

        assertNotNull(admin.getId());
        assertNotNull(memberRepository.findById(admin.getId()));
    }

    void initCityAndDistrict() {
        city = cityRepository.save(City.of("서울특별시"));
        district = districtRepository.save(District.of("종로구", city));
        assertNotNull(cityRepository.findByName("서울특별시"));
        assertNotNull(districtRepository.findByCityAndName(city, "종로구"));
        assertNotNull(city.getId());
        assertNotNull(district.getId());
    }

    @DisplayName(AUTH_CODE_SEND)
    @Test
    void sendAuthCodeTest() {
        //given
        String signupMobile = "01012341234";
        AuthPurposeType authPurposeType = AuthPurposeType.SIGNUP;
        VerificationCodeSendRequest codeSendRequest = new VerificationCodeSendRequest(
                signupMobile, authPurposeType
        );

        // when
        authService.sendAuthCode(codeSendRequest);

        // then
        verify(smsProvider).send(eq(signupMobile), anyString(), eq(SmsType.AUTH_CODE));

        String cachedCode = cacheAccessor.get(CacheType.SIGNUP_CODE, signupMobile, String.class)
                .orElseThrow(
                        () -> new AuthException(ErrorMessage.AUTH_NOT_VERIFIED)
                );
        assertNotNull(cachedCode);
        log.info("cached authCode : {}", cachedCode);
    }

    @DisplayName(AUTH_CODE_VERIFY)
    @Test
    void verifyCodeTest() {
        String signupMobile = "01012341234";
        AuthPurposeType authPurposeType = AuthPurposeType.SIGNUP;
        VerificationCodeSendRequest codeSendRequest = new VerificationCodeSendRequest(
                signupMobile, authPurposeType
        );

        authService.sendAuthCode(codeSendRequest);

        verify(smsProvider).send(eq(signupMobile), anyString(), eq(SmsType.AUTH_CODE));

        String cachedCode = cacheAccessor.get(
                CacheType.SIGNUP_CODE, signupMobile, String.class
        ).orElseThrow(
                () -> new AuthException(ErrorMessage.AUTH_NOT_VERIFIED)
        );

        VerificationCodeVerifyRequest verifyRequest = new VerificationCodeVerifyRequest(
                signupMobile, cachedCode, authPurposeType
        );

        authService.verifyCode(verifyRequest);

        boolean isCachedVerified = cacheAccessor.get(
                CacheType.SIGNUP_VERIFIED,
                signupMobile,
                Boolean.class
        ).orElseGet(() -> false);

        log.info("isCachedVerified :: {}", isCachedVerified);
        assertTrue(isCachedVerified);

        String savedCachedAuthCode = cacheAccessor.get(
                CacheType.SIGNUP_CODE,
                signupMobile,
                String.class
        ).orElseGet(() -> null);

        assertNull(savedCachedAuthCode);
    }

    /**
     * ● 회원가입 시나리오 통합 테스트
     *
     * <p> 시나리오
     * <ol>
     *     <li> 인증 코드 요청 (SMS) => sendAuthCode()</li>
     *     <li> 인증 코드 검증 => verifyCode() </li>
     *     <li> 회원가입 처리 => signupMember() </li>
     * </ol>
     */
    @DisplayName(MEMBER_SIGNUP_SCENARIO)
    @Test
    void memberSignupIntegrationTest() {

        /**
         * sendAuthCode
         * 인증 번호 전송(요쳥된 휴대폰 번호로)
         * 인증 번호 및 휴대폰 번호 캐시 저장
         */
        String signupMobile = "01012341234";
        AuthPurposeType authPurposeType = AuthPurposeType.SIGNUP;
        VerificationCodeSendRequest codeSendRequest = new VerificationCodeSendRequest(
                signupMobile, authPurposeType
        );
        authService.sendAuthCode(codeSendRequest);

        verify(smsProvider).send(eq(signupMobile), anyString(), eq(SmsType.AUTH_CODE));

        String authCode = cacheAccessor.get(CacheType.SIGNUP_CODE, signupMobile, String.class)
                .orElseThrow(() -> new AuthException(ErrorMessage.AUTH_NOT_VERIFIED));

        /**
         * verifyCode
         * 인증 번호 검증 (sendAuthCode 에서 저장된 캐시 데이터와 비교)
         * 휴대폰 번호 인증 확인 캐시 저장
         */
        VerificationCodeVerifyRequest verifyRequest = new VerificationCodeVerifyRequest(
                signupMobile, authCode, authPurposeType
        );

        authService.verifyCode(verifyRequest);

        boolean isCachedVerified = false;
        isCachedVerified = cacheAccessor.get(
                CacheType.SIGNUP_VERIFIED,
                signupMobile,
                Boolean.class
        ).orElseGet(() -> false);
        assertTrue(isCachedVerified);

        log.info("Before signup isCachedVerified :: {}", isCachedVerified);

        /**
         * signupMember
         * 인증 번호 검증(verifyCode 에서 인증된 휴대폰 번호 (CacheType.SIGNUP_VERIFIED))
         * 정상 회원 가입 및 회원 정상 저장 검증
         */

        LocalDate birth = LocalDate.of(1994, 3, 16);
        MemberSignUpRequest signUpRequest = new MemberSignUpRequest(
                "김회원",
                "01012341234",
                Gender.MALE,
                birth,
                "1231231!@",
                1L,
                1L
        );

        MemberSignupResponse response = authService.signupMember(signUpRequest);

        assertNotNull(response);

        isCachedVerified = cacheAccessor.get(
                CacheType.SIGNUP_VERIFIED,
                signupMobile,
                Boolean.class
        ).orElseGet(() -> false);
        assertFalse(isCachedVerified);

        log.info("After signup isCachedVerified :: {}", isCachedVerified);


        MemberEntity savedMember = memberRepository.findByMobile(signUpRequest.getMobile()).orElseThrow();
        assertNotNull(savedMember);
        int old = AgeUtil.calculate(savedMember.getBirth());

        log.info("savedMember name : {}", savedMember.getName());
        log.info("savedMember old : {}", AgeUtil.calculate(savedMember.getBirth()));
        log.info("savedMember mobile : {}", savedMember.getMobile());

        log.info("response name : {}", response.getName());
        log.info("response old : {}", response.getOld());
        log.info("response mobile : {}", response.getMobile());

        assertEquals(savedMember.getName(), response.getName());
        assertEquals(savedMember.getMobile(), response.getMobile());
        assertEquals(savedMember.getStatus(), MemberStatus.PENDING);
        assertEquals(old, response.getOld());
    }

    /**
     * ● 용병 등록 시나리오 통합 테스트
     *
     * <p> 시나리오
     * <ol>
     *     <li> 인증 코드 요청(SMS) => sendAuthCode() </li>
     *     <li> 인증 코드 검증 => verifyCode() </li>
     *     <li> 용병 등록 처리 => registerMercenary() </li>
     * </ol>
     */

    @DisplayName(MERCENARY_REGISTER_SCENARIO)
    @Test
    void registerMercenaryTest() {

        /**
         * sendAuthCode
         * 인증 번호 전송(요쳥된 휴대폰 번호로)
         * 인증 번호 및 휴대폰 번호 캐시 저장
         */
        String registerMobile = "01012341234";
        AuthPurposeType authPurposeType = AuthPurposeType.SIGNUP;
        VerificationCodeSendRequest codeSendRequest = new VerificationCodeSendRequest(
                registerMobile, authPurposeType
        );
        authService.sendAuthCode(codeSendRequest);

        verify(smsProvider).send(eq(registerMobile), anyString(), eq(SmsType.AUTH_CODE));

        String authCode = cacheAccessor.get(CacheType.SIGNUP_CODE, registerMobile, String.class)
                .orElseThrow(() -> new AuthException(ErrorMessage.AUTH_NOT_VERIFIED));

        /**
         * verifyCode
         * 인증 번호 검증 (sendAuthCode 에서 저장된 캐시 데이터와 비교)
         * 휴대폰 번호 인증 확인 캐시 저장
         */
        VerificationCodeVerifyRequest verifyRequest = new VerificationCodeVerifyRequest(
                registerMobile, authCode, authPurposeType
        );

        authService.verifyCode(verifyRequest);

        boolean isCachedVerified;
        isCachedVerified = cacheAccessor.get(
                CacheType.SIGNUP_VERIFIED,
                registerMobile,
                Boolean.class
        ).orElseGet(() -> false);
        assertTrue(isCachedVerified);

        log.info("Before register isCachedVerified :: {}", isCachedVerified);

        /**
         * registerMercenary
         * 인증 번호 검증(verifyCode 에서 인증된 휴대폰 번호 (CacheType.SIGNUP_VERIFIED))
         * 정상 용병 등록 및 용병 정상 저장
         */
        MercenaryRetentionStatus retentionStatus = MercenaryRetentionStatus.KEEP;
        MercenaryRegistrationRequest registrationRequest = new MercenaryRegistrationRequest(
                "김용병",
                registerMobile,
                Gender.MALE,
                retentionStatus
        );

        MercenaryRegistrationResponse response = authService.registerMercenary(
                registrationRequest
        );

        isCachedVerified = cacheAccessor.get(
                CacheType.SIGNUP_VERIFIED,
                registerMobile,
                Boolean.class
        ).orElseGet(() -> false);
        assertFalse(isCachedVerified);

        log.info("After signup isCachedVerified :: {}", isCachedVerified);

        assertNotNull(response);
        MercenaryEntity savedMercenary = mercenaryRepository
                .findByMobile(registrationRequest.getMobile()).orElseThrow(
                        () -> new NotFoundException(ErrorMessage.MERCENARY_NOT_FOUND)
                );
        assertNotNull(savedMercenary);
        log.info("registerMercenary response name : {}", response.getName());
        log.info("registerMercenary response mobile : {}", response.getMobile());
        log.info("savedMercenary name : {}", savedMercenary.getName());
        log.info("savedMember mobile : {}", savedMercenary.getMobile());
        assertEquals(savedMercenary.getName(), response.getName());
        assertEquals(savedMercenary.getMobile(), response.getMobile());
        assertEquals(savedMercenary.getStatus(), MercenaryStatus.PENDING);
    }

    @DisplayName(DUPLICATED_AUTH_CODE)
    @Test
    void sendAuthCodeDuplicatedErrorTest() {
        // already exists admin mobile(01012340001) (상단 case 문 createAdmin)
        VerificationCodeSendRequest request = new VerificationCodeSendRequest(
                "01012340001",
                AuthPurposeType.SIGNUP
        );

        DuplicatedException exception = assertThrows(DuplicatedException.class, () -> {
            authService.sendAuthCode(request);
        });

        assertEquals(ErrorMessage.AUTH_USER_DUPLICATED.code(), exception.error().code());
        log.info("ErrorMessage code :: {}", ErrorMessage.AUTH_USER_DUPLICATED.code());
        log.info("exception code :: {}", exception.error().code());

        assertEquals(ErrorMessage.AUTH_USER_DUPLICATED.toString(), exception.getMessage());
        log.info("ErrorMessage toString() :: {}", ErrorMessage.AUTH_USER_DUPLICATED);
        log.info("exception Message :: {}", exception.getMessage());
    }

    @DisplayName(MEMBER_LOGIN)
    @Test
    void memberLoginTest() {
        LocalDate birth = LocalDate.of(1994, 3, 16);
        String rawPassword = "rawPassword";
        MemberSignUpRequest signup = new MemberSignUpRequest(
                "testMember", "01012341234",
                Gender.MALE, birth,
                rawPassword, 1L, 1L
        );

        MemberEntity member = MemberEntity.create(signup, city, district);
        memberRepository.save(member);
        member.updateStatus(MemberStatus.ENABLED);

        MemberLoginRequest loginRequest = new MemberLoginRequest("01012341234", rawPassword);
        String userAgent = "Mozilla/5.0 ...";
        String token = authService.loginMember(userAgent, loginRequest);

        assertNotNull(token);

        log.info("token : {}", token);

        String subjectId = Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        log.info("member Id : {}", member.getId());
        log.info("JWT subject Id : {}", subjectId);
        assertEquals(member.getId(), subjectId);
    }

    @DisplayName(MERCENARY_LOGIN)
    @Test
    void mercenaryLoginTest() {

        /** Create Mercenary, Check Exists Mercenary */
        MercenaryRegistrationRequest registration = new MercenaryRegistrationRequest(
                "testMercenary",
                "01012341234",
                Gender.MALE,
                MercenaryRetentionStatus.KEEP
        );

        MercenaryEntity mercenary = MercenaryEntity.create(registration);

        mercenaryRepository.save(mercenary);

        assertNotNull(mercenaryRepository.findByMobile(registration.getMobile()));

        /** Create matchVote, match */
        LocalDate now = LocalDate.now();
        LocalDate endAt = now.plusDays(1);
        LocalDate matchAt = now.plusDays(2);
        MatchVoteCreateRequest matchVoteCreateRequest = new MatchVoteCreateRequest(
                "matchVote title",
                MatchType.FUTSAL,
                matchAt,
                endAt,
                VoteModeType.SINGLE
        );

        MatchVoteEntity matchVote = MatchVoteEntity.create(
                admin, matchVoteCreateRequest, Arrays.asList(admin)
        );

        matchVoteRepository.save(matchVote);

        assertNotNull(matchVoteRepository.findById(matchVote.getId()));
        assertNotNull(matchVote.getMatch());

        String mercenaryCode = CodeGeneratorUtil.getMercenaryCode();

        MatchEntity match = matchVote.getMatch();

        MercenaryTransientEntity transientEntity = MercenaryTransientEntity.create(
                mercenaryCode, match
        );

        mercenaryTransientRepository.save(transientEntity);
        mercenary.updateTransient(transientEntity);

        assertNotNull(mercenary.getMercenaryTransient());
        assertNotNull(mercenary.getMercenaryTransient().getId());

        assertTrue(EncryptUtil.matches(mercenaryCode, mercenary.getMercenaryTransient().getCode()));
        assertEquals(mercenary.getMercenaryTransient().getMatch(), match);

        log.info("transientEntity matchId : {}", transientEntity.getMatch().getId());
        log.info("match Id : {}", match.getId());

        mercenary.updateStatus(MercenaryStatus.ENABLED);

        String userAgent = "Mozilla/5.0 ...";
        MercenaryLoginRequest loginRequest = new MercenaryLoginRequest(
                registration.getMobile(),
                mercenaryCode
        );

        String token = authService.loginMercenary(userAgent, loginRequest);

        assertNotNull(token);

        String subjectId = Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        assertEquals(mercenary.getId(), subjectId);

        log.info("Mercenary Id : {}", mercenary.getId());
        log.info("JWT subject Id : {}", subjectId);
    }

    /**
     * ● 회원 비밀 번호 재설정 시나리오 통합 테스트
     *
     * <p> 시나리오[
     * <ol>
     *     <li> 임의 회원 등록(회원 가입 과정 생략) => signupMember() </li>
     *     <li> 인증 코드 요청(SMS) => sendAuthCode() </li>
     *     <li> 인증 코드 검증 => verifyCode() </li>
     *     <li> 캐시 검증(sendAuthCode(), verifyCode() 각각 검증) </li>
     *     <li> 회원 비밀 번호 재설정 => resetMemberPassword() </li>
     *     <li> 기존 비밀 번호와 비매칭 검증 </li>
     * </ol>
     */
    @DisplayName(MEMBER_PASSWORD_RESET_SCENARIO)
    @Test
    void resetMemberPasswordTest() {

        /** Create Temporary Member  */
        String memberTestMobile = "01012341234";
        cacheAccessor.put(CacheType.SIGNUP_VERIFIED, memberTestMobile, true);

        MemberSignUpRequest signUpRequest = new MemberSignUpRequest(
                "김회원",
                memberTestMobile,
                Gender.MALE,
                LocalDate.of(1994, 3, 17),
                "1231231!@",
                1L,
                1L
        );


        MemberEntity member = MemberEntity.create(
                signUpRequest, city, district
        );

        memberRepository.save(member);
        assertNotNull(member.getId());

        /**
         * sendAuthCode
         * 인증 번호 전송(요쳥된 휴대폰 번호로)
         * 인증 번호 및 휴대폰 번호 캐시 저장
         */
        AuthPurposeType authPurposeType = AuthPurposeType.RESET_PASSWORD;

        VerificationCodeSendRequest codeSendRequest = new VerificationCodeSendRequest(
                memberTestMobile, authPurposeType
        );

        authService.sendAuthCode(codeSendRequest);
        verify(smsProvider).send(eq(memberTestMobile), anyString(), eq(SmsType.AUTH_CODE));

        String cachedCode = cacheAccessor.get(CacheType.RESET_CODE, memberTestMobile, String.class)
                .orElseThrow(
                        () -> new AuthException(ErrorMessage.AUTH_NOT_VERIFIED)
                );

        /**
         * verifyCode
         * 인증 번호 검증 (sendAuthCode 에서 저장된 캐시 데이터와 비교)
         * 휴대폰 번호 인증 확인 캐시 저장
         */
        VerificationCodeVerifyRequest verifyRequest = new VerificationCodeVerifyRequest(
                memberTestMobile, cachedCode, authPurposeType
        );

        authService.verifyCode(verifyRequest);

        boolean isCachedVerified = cacheAccessor.get(
                CacheType.RESET_VERIFIED,
                memberTestMobile,
                Boolean.class
        ).orElseGet(() -> false);

        log.info("Before resetPassword isCachedVerified :: {}", isCachedVerified);
        assertTrue(isCachedVerified);

        /**
         * resetMemberPassword
         * 인증된 휴대폰 번호 검증
         * 비밀 번호 변경 및 휴대폰 인증 캐시 제거
         * 바뀐 비밀 번호와 회원 비밀 번호 match 검증
         */
        String updatePassword = "123123!!";
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(
                memberTestMobile, updatePassword
        );

        authService.resetMemberPassword(resetPasswordRequest);
        isCachedVerified = cacheAccessor.get(
                CacheType.RESET_VERIFIED,
                memberTestMobile,
                Boolean.class
        ).orElseGet(() -> false);
        log.info("After resetPassword isCachedVerified :: {}", isCachedVerified);
        assertFalse(isCachedVerified);

        assertTrue(EncryptUtil.matches(updatePassword, member.getPassword()));
    }


    /**
     * ● 용병 코드 재발급 시나리오 통합 테스트
     *
     * <p> 시나리오
     * <ol>
     *     <li> 임의 용병 계정 등록(용병 등록 과정 생략) => registerMercenary() </li>
     *     <li> 임의 매칭 투표 및 용병 코드 생성(관리자 승인 프로세스 수동 생성) (MercenaryTransientEntity Set) </li>
     *     <li> 캐시 검증(sendAuthCode(), verifyCode() 생략 및 임의 cacheCode 저장 (Put cacheAccessor cacheCode)) </li>
     *     <li> 용병 코드 인증 캐시 검증 후 재발급 => resetMercenaryCode() </li>
     *     <li> 기존 용병 코드와 비매칭 검증 </li>
     * </ol>
     */
    @DisplayName(MERCENARY_CODE_RESET_SCENARIO)
    @Test
    void resetMercenaryCodeTest() {

        /** Create Temporary Mercenary */
        String mercenaryTestMobile = "01012341234";
        cacheAccessor.put(CacheType.SIGNUP_VERIFIED, mercenaryTestMobile, true);

        MercenaryRetentionStatus retentionStatus = MercenaryRetentionStatus.KEEP;
        MercenaryRegistrationRequest registrationRequest = new MercenaryRegistrationRequest(
                "김용병",
                mercenaryTestMobile,
                Gender.MALE,
                retentionStatus
        );
        MercenaryEntity mercenary = MercenaryEntity.create(registrationRequest);
        mercenaryRepository.save(mercenary);

        assertNotNull(mercenaryRepository.findByMobile(mercenaryTestMobile));

        /**
         * 임의 매치 투표 생성 (매치 투표 생성 시 자동 매치(MatchEntity) 생성) => MatchVoteEntity 생성
         * 관리자 임의(수동) 승인 (MercenaryTransientEntity (code, match) 임의 생성 후 updateTransient)
         * 인증 번호 전송(요쳥된 휴대폰 번호로)
         * 인증 번호 및 휴대폰 번호 캐시 저장
         */

        assertNull(mercenary.getMercenaryTransient());
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String originMercenaryCode = "test123456";

        MatchVoteCreateRequest matchVoteCreateRequest = new MatchVoteCreateRequest(
                "testTitle",
                MatchType.FUTSAL,
                tomorrow.plusDays(1),
                tomorrow,
                VoteModeType.SINGLE
        );

        List<UserEntity> rawVoters = Arrays.asList(admin);
        MatchVoteEntity matchVote = MatchVoteEntity.create(
                admin,
                matchVoteCreateRequest,
                rawVoters
        );
        matchVoteRepository.save(matchVote);

        MercenaryTransientEntity transientEntity = MercenaryTransientEntity.create(
                originMercenaryCode, matchVote.getMatch()
        );
        mercenaryTransientRepository.save(transientEntity);

        mercenary.updateStatus(MercenaryStatus.ENABLED);
        mercenary.updateTransient(transientEntity);

        /**
         * sendAuthCode, verifyCode 로직 생략 및 인증Cache 저장 (verifyCode 마지막 행 로직만 실행)
         * 인증 번호 및 휴대폰 번호 캐시 저장
         */

        cacheAccessor.put(CacheType.RESET_VERIFIED, mercenaryTestMobile, true);

        ResetMercenaryCodeRequest resetMercenaryCodeRequest = new ResetMercenaryCodeRequest(
                mercenaryTestMobile
        );

        authService.resetMercenaryCode(resetMercenaryCodeRequest);

        log.info("After reset mercenaryCode matching result :: {}",
                EncryptUtil.matches(
                        originMercenaryCode,
                        mercenary.getMercenaryTransient().getCode()
                )
        );
        assertFalse(
                EncryptUtil.matches(
                        originMercenaryCode,
                        mercenary.getMercenaryTransient().getCode()
                )
        );
    }

}
