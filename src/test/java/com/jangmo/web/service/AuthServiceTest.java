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
import com.jangmo.web.exception.AuthException;
import com.jangmo.web.exception.NotFoundException;

import com.jangmo.web.infra.cache.CacheAccessor;
import com.jangmo.web.infra.sms.SmsProvider;
import com.jangmo.web.model.dto.request.*;

import com.jangmo.web.model.dto.request.vote.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.response.vote.MatchVoteCreateResponse;
import com.jangmo.web.model.dto.response.MemberSignupResponse;

import com.jangmo.web.model.dto.response.MercenaryRegistrationResponse;
import com.jangmo.web.model.entity.MatchEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.model.entity.user.MercenaryTransientEntity;

import com.jangmo.web.repository.*;

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
        verify(smsProvider).send(eq(signupMobile), anyString(), eq(SmsType.AUTH_CODE));

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

        verify(smsProvider).send(eq(signupMobile), anyString(), eq(SmsType.AUTH_CODE));

        String cachedCode = cacheAccessor.get(CacheType.SIGNUP_CODE, signupMobile, String.class)
                .orElseThrow(
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
    @DisplayName("회원 가입 시나리오 테스트")
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
    @DisplayName("용병 등록 시나리오 테스트")
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
    void sendAuthCodeDuplicatedErrorTest() {
        // already exists admin mobile(01043053451)
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

    @DisplayName("용병 로그인 테스트")
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
                MatchType.FUTSAL,
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

    /**
     * ● 회원 비밀 번호 재설정 시나리오 통합 테스트
     *
     * <p> 시나리오
     * <ol>
     *     <li> 임의 회원 등록(회원 가입 과정 생략) => signupMember() </li>
     *     <li> 인증 코드 요청(SMS) => sendAuthCode() </li>
     *     <li> 인증 코드 검증 => verifyCode() </li>
     *     <li> 캐시 검증(sendAuthCode(), verifyCode() 각각 검증) </li>
     *     <li> 회원 비밀 번호 재설정 => resetMemberPassword() </li>
     *     <li> 기존 비밀 번호와 비매칭 검증 </li>
     * </ol>
     */
    @DisplayName("회원 비밀 번호 재설정 시나리오 테스트")
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
        MemberEntity member = memberRepository.findByMobile(memberTestMobile)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND)
                );
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
     *     <li> 인증 코드 요청(SMS) => sendAuthCode() </li>
     *     <li> 인증 코드 검증 => verifyCode() </li>
     *     <li> 캐시 검증(sendAuthCode(), verifyCode() 각각 검증) </li>
     *     <li> 용병 코드 인증 캐시 검증 후 재발급 => resetMercenaryCode() </li>
     *     <li> 기존 용병 코드와 비매칭 검증 </li>
     * </ol>
     */
    @DisplayName("용병 코드 재발급 시나리오 테스트")
    @Test
    @Transactional
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
        authService.registerMercenary(registrationRequest);

        MercenaryEntity mercenary = mercenaryRepository.findByMobile(mercenaryTestMobile)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.MERCENARY_NOT_FOUND)
                );

        assertNotNull(mercenary);
        assertEquals(mercenary.getStatus(), MercenaryStatus.PENDING);

        /**
         * 임의 매치 투표 생성 (매치 투표 생성 시 자동 매치(MatchEntity) 생성) => MatchVoteEntity 생성
         * 관리자 임의(수동) 승인 (MercenaryTransientEntity (code, match) 임의 생성 후 updateTransient)
         * 인증 번호 전송(요쳥된 휴대폰 번호로)
         * 인증 번호 및 휴대폰 번호 캐시 저장
         */

        assertNull(mercenary.getMercenaryTransient());
        UserEntity admin = memberRepository.findByMobile("01043053451").get();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String originMercenaryCode = "test123456";

        MatchVoteCreateRequest matchVoteCreateRequest = new MatchVoteCreateRequest(
                MatchType.FUTSAL, tomorrow.plusDays(1), tomorrow
        );

        MatchVoteEntity matchVote = MatchVoteEntity.create(
                admin, matchVoteCreateRequest
        );
        matchVoteRepository.save(matchVote);

        MercenaryTransientEntity transientEntity = MercenaryTransientEntity.create(
                originMercenaryCode, matchVote.getMatch()
        );
        mercenaryTransientRepository.save(transientEntity);

        mercenary.updateStatus(MercenaryStatus.ENABLED);
        mercenary.updateTransient(transientEntity);

        /**
         * sendAuthCode
         * 인증 번호 전송(요쳥된 휴대폰 번호로)
         * 인증 번호 및 휴대폰 번호 캐시 저장
         */
        AuthPurposeType authPurposeType = AuthPurposeType.RESET_MERCENARY_CODE;

        VerificationCodeSendRequest codeSendRequest = new VerificationCodeSendRequest(
                mercenaryTestMobile, authPurposeType
        );

        authService.sendAuthCode(codeSendRequest);

        verify(smsProvider).send(eq(mercenaryTestMobile), anyString(), eq(SmsType.AUTH_CODE));

        String cachedCode = cacheAccessor.get(CacheType.RESET_CODE, mercenaryTestMobile, String.class)
                .orElseThrow(
                        () -> new AuthException(ErrorMessage.AUTH_NOT_VERIFIED)
                );

        VerificationCodeVerifyRequest verifyRequest = new VerificationCodeVerifyRequest(
                mercenaryTestMobile, cachedCode, authPurposeType
        );

        authService.verifyCode(verifyRequest);

        boolean isCachedVerified = cacheAccessor.get(
                CacheType.RESET_VERIFIED,
                mercenaryTestMobile,
                Boolean.class
        ).orElseGet(() -> false);

        log.info("Before reset mercenaryCode isCachedVerified :: {}", isCachedVerified);
        assertTrue(isCachedVerified);

        log.info("Before reset mercenaryCode matching result :: {}",
                EncryptUtil.matches(
                        originMercenaryCode,
                        mercenary.getMercenaryTransient().getCode()
                )
        );
        assertTrue(
                EncryptUtil.matches(
                        originMercenaryCode,
                        mercenary.getMercenaryTransient().getCode()
                )
        );

        ResetMercenaryCodeRequest resetMercenaryCodeRequest = new ResetMercenaryCodeRequest(
                mercenaryTestMobile
        );

        authService.resetMercenaryCode(resetMercenaryCodeRequest);
        verify(smsProvider).send(eq(mercenaryTestMobile), anyString(), eq(SmsType.MERCENARY_CODE));
        isCachedVerified = cacheAccessor.get(
                CacheType.RESET_VERIFIED,
                mercenaryTestMobile,
                Boolean.class
        ).orElseGet(() -> false);

        assertFalse(isCachedVerified);

        log.info("After reset mercenaryCode isCachedVerified :: {}", isCachedVerified);

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
