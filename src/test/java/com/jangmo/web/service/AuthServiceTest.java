package com.jangmo.web.service;

import com.jangmo.web.config.jwt.JwtConfig;
import com.jangmo.web.constants.AuthPurposeType;
import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.cache.CacheType;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.MercenaryRetentionStatus;
import com.jangmo.web.constants.user.MercenaryStatus;
import com.jangmo.web.constants.match.MatchType;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.AuthException;
import com.jangmo.web.exception.NotFoundException;

import com.jangmo.web.infra.cache.CacheAccessor;
import com.jangmo.web.infra.sms.SmsProvider;
import com.jangmo.web.model.dto.request.*;

import com.jangmo.web.model.dto.response.MatchVoteCreateResponse;
import com.jangmo.web.model.dto.response.MemberSignupResponse;

import com.jangmo.web.model.dto.response.MercenaryRegistrationResponse;
import com.jangmo.web.model.entity.MatchEntity;
import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.model.entity.user.MercenaryTransientEntity;

import com.jangmo.web.repository.MemberRepository;
import com.jangmo.web.repository.MercenaryRepository;
import com.jangmo.web.repository.CityRepository;
import com.jangmo.web.repository.DistrictRepository;
import com.jangmo.web.repository.MatchVoteRepository;
import com.jangmo.web.repository.MercenaryTransientRepository;

import com.jangmo.web.service.manager.VoteServiceImpl;
import com.jangmo.web.utils.AgeUtil;

import com.jangmo.web.utils.CodeGeneratorUtil;
import com.jangmo.web.utils.EncryptUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@Slf4j
@SpringBootTest
public class AuthServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MercenaryRepository mercenaryRepository;
    @Autowired
    CityRepository cityRepository;
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    MatchVoteRepository matchVoteRepository;
    @Autowired
    MercenaryTransientRepository mercenaryTransientRepository;
    @Autowired
    VoteServiceImpl voteService;
    @Autowired
    JwtConfig jwtConfig;
    @Autowired
    AuthServiceImpl authService;

    @Autowired
    CacheAccessor cacheAccessor;

    @MockBean
    private SmsProvider smsProvider;

    @BeforeEach
    void init(TestInfo testInfo) {
        if (testInfo.getDisplayName().equals("Member 로그인 테스트") ||
            testInfo.getDisplayName().equals("Mercenary 로그인 테스트")) {
            ReflectionTestUtils.setField(
                    jwtConfig,
                    "secret",
                    //test_json_web_token_secret_key_for_hmac
                    "dGVzdF9qc29uX3dlYl90b2tlbl9zZWNyZXRfa2V5X2Zvcl9obWFj"
            );
        }
    }

    @DisplayName("인증 코드 전송 및 휴대폰 번호 캐시 저장 결과 테스트")
    @Test
    @Transactional
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
        verify(smsProvider).send(eq(signupMobile), anyString(), any());

        String cachedCode = cacheAccessor.get(CacheType.SIGNUP_CODE, signupMobile, String.class).orElseThrow(
                () -> new AuthException(ErrorMessage.AUTH_NOT_VERIFIED)
        );
        assertNotNull(cachedCode);
        log.info("cached authCode : {}", cachedCode);
    }

    @DisplayName("인증 코드 검증 및 휴대폰 인증 여부 캐시 저장 테스트")
    @Test
    @Transactional
    void verifyCodeTest() {
        String signupMobile = "01012341234";
        AuthPurposeType authPurposeType = AuthPurposeType.SIGNUP;
        VerificationCodeSendRequest codeSendRequest = new VerificationCodeSendRequest(
                signupMobile, authPurposeType
        );

        authService.sendAuthCode(codeSendRequest);

        String cachedCode = cacheAccessor.get(CacheType.SIGNUP_CODE, signupMobile, String.class).orElseThrow(
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
     * 회원가입(Member) 시나리오 통합 테스트
     *
     * <p>시나리오:
     * <ol>
     *     <li>인증 코드 요청 (SMS) - sendAuthCode()</li>
     *     <li>인증 코드 검증 - verifyCode() </li>
     *     <li>회원가입 처리 signupMember() </li>
     *     <li>캐시 만료 시 예외 처리</li>
     * </ol>
     *
     * 테스트 목적:
     * - 인증 절차와 회원가입 및 용병 등록 흐름의 유효성 검증
     * - 캐시 TTL 적용 확인
     */
    @DisplayName("회원 가입 시나리오 및 인증된 휴대폰 캐시 검증 테스트")
    @Test
    @Transactional
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
        verify(smsProvider).send(eq(signupMobile), anyString(), any());

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

    @DisplayName("용병 등록 시나리오 및 인증된 휴대폰 캐시 검증 테스트")
    @Test
    @Transactional
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

        verify(smsProvider).send(eq(registerMobile), anyString(), any());

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

        boolean isCachedVerified = false;
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

    @DisplayName("인증 번호 요청 중복 에러 테스트")
    @Test
    @Transactional
    void signupDuplicatedTest() {
        VerificationCodeSendRequest request = new VerificationCodeSendRequest(
                "01043053451",
                AuthPurposeType.SIGNUP
        );
        authService.sendAuthCode(request);
    }

    @DisplayName("Member 로그인 테스트")
    @Test
    @Transactional
    void memberLoginTest() {
        LocalDate birth = LocalDate.of(1994, 3, 16);
        String rawPassword = "rawPassword";
        MemberSignUpRequest signup = new MemberSignUpRequest(
                "testMember",
                "01012341234",
                Gender.MALE,
                birth,
                rawPassword,
                1L,
                1L
        );

        authService.signupMember(signup);
        MemberEntity signupMember = memberRepository.findByMobile(signup.getMobile()).orElseThrow();
        signupMember.updateStatus(MemberStatus.ENABLED);

        MemberLoginRequest loginRequest = new MemberLoginRequest("01012341234", rawPassword);
        String userAgent = "Mozilla/5.0 ...";
        String token = authService.loginMember(userAgent, loginRequest);

        String secret = "dGVzdF9qc29uX3dlYl90b2tlbl9zZWNyZXRfa2V5X2Zvcl9obWFj";
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        assertNotNull(token);

        log.info("token : " + token);

        String userId = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        log.info("userId : " + userId);
        assertEquals(signupMember.getId(), userId);
    }

    @DisplayName("Mercenary 로그인 테스트")
    @Test
    @Transactional
    void mercenaryLoginTest() {

        /** Create Mercenary, Check Exists Mercenary */
        MercenaryRegistrationRequest registration = new MercenaryRegistrationRequest(
                "testName",
                "01012341234",
                Gender.MALE,
                MercenaryRetentionStatus.KEEP
        );

        MercenaryEntity mercenary = mercenaryRepository.save(
                MercenaryEntity.create(registration)
        );
        assertNotNull(mercenaryRepository.findByMobile(registration.getMobile()));

        /** Create matchVote, match */
        MemberEntity admin = memberRepository.findByMobile("01043053451").get();
        LocalDate now = LocalDate.now();
        LocalDate endAt = now.plusDays(1);
        LocalDate matchAt = now.plusDays(2);
        MatchVoteCreateRequest matchVoteCreateRequest = new MatchVoteCreateRequest(
                MatchType.REGULAR,
                matchAt,
                endAt
        );

        MatchVoteCreateResponse matchVoteResponse = voteService.createMatchVote(
                admin.getId(),
                matchVoteCreateRequest
        );
        assertNotNull(matchVoteResponse);
        MatchVoteEntity matchVote = matchVoteRepository.findByMatchAt(matchVoteResponse.getMatchAt()).get(0);
        assertNotNull(matchVote);
        assertNotNull(matchVote.getMatch());

        String mercenaryCode = CodeGeneratorUtil.getMercenaryCode();

        MatchEntity match = matchVote.getMatch();

        MercenaryTransientEntity transientEntity = mercenaryTransientRepository.save(
                MercenaryTransientEntity.create(
                        mercenaryCode,
                        match
                )
        );
        mercenary.updateTransient(transientEntity);
        assertNotNull(transientEntity);
        assertNotNull(mercenary.getMercenaryTransient());
        assertNotNull(transientEntity.getCode());
        assertNotNull(transientEntity.getMatch());

        assertEquals(transientEntity.getMatch(), match);

        log.info("transientEntity matchId : {}", transientEntity.getMatch().getId());
        log.info("match Id : {}", match.getId());

        mercenary.updateStatus(MercenaryStatus.ENABLED);
        assertEquals(mercenary.getStatus(), MercenaryStatus.ENABLED);
        log.info("mercenary status : " + mercenary.getStatus());

        String userAgent = "Mozilla/5.0 ...";
        MercenaryLoginRequest loginRequest = new MercenaryLoginRequest(
                registration.getMobile(),
                mercenaryCode
        );
        String token = authService.loginMercenary(userAgent, loginRequest);
        assertNotNull(token);



        String secret = "dGVzdF9qc29uX3dlYl90b2tlbl9zZWNyZXRfa2V5X2Zvcl9obWFj";
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        String mercenaryId = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        assertEquals(mercenary.getId(), mercenaryId);
        log.info("mercenary getId : {}", mercenary.getId());
        log.info("mercenaryId : {}", mercenaryId);
    }

    @DisplayName("회원 비밀번호 재설정 시나리오 테스트")
    @Test
    @Transactional
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
        authService.signupMember(signUpRequest);

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

        String cachedCode = cacheAccessor.get(CacheType.RESET_CODE, memberTestMobile, String.class).orElseThrow(
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
        MemberEntity mercenary = memberRepository.findByMobile(memberTestMobile).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND)
        );

        assertTrue(EncryptUtil.matches(updatePassword, mercenary.getPassword()));

    }


}
