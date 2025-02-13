package com.jangmo.web.service;

import com.jangmo.web.config.jwt.JwtConfig;
import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MemberStatus;
import com.jangmo.web.constants.MercenaryRetentionStatus;
import com.jangmo.web.constants.MercenaryStatus;
import com.jangmo.web.constants.match.MatchType;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.custom.NotFoundException;

import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.dto.request.MemberLoginRequest;
import com.jangmo.web.model.dto.request.MercenaryRegistrationRequest;
import com.jangmo.web.model.dto.request.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.request.MercenaryLoginRequest;

import com.jangmo.web.model.dto.response.MatchVoteCreateResponse;
import com.jangmo.web.model.dto.response.MemberSignupResponse;
import com.jangmo.web.model.dto.response.MercenaryRegistrationResponse;
import com.jangmo.web.model.entity.MatchEntity;
import com.jangmo.web.model.entity.MatchVoteEntity;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.model.entity.user.MercenaryTransientEntity;

import com.jangmo.web.repository.*;

import com.jangmo.web.service.manager.UserManagementServiceImpl;
import com.jangmo.web.service.manager.VoteServiceImpl;
import com.jangmo.web.utils.AgeUtil;

import com.jangmo.web.utils.CodeGeneratorUtil;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
@SpringBootTest
public class AuthServiceTest {

    @Autowired MemberRepository memberRepository;
    @Autowired MercenaryRepository mercenaryRepository;
    @Autowired CityRepository cityRepository;
    @Autowired DistrictRepository districtRepository;
    @Autowired MatchVoteRepository matchVoteRepository;

    @Autowired MercenaryTransientRepository mercenaryTransientRepository;

    @Autowired AuthServiceImpl authService;

    @Autowired UserManagementServiceImpl userManagementService;

    @Autowired VoteServiceImpl voteService;
    @Autowired JwtConfig jwtConfig;

    @BeforeEach
    void init(TestInfo testInfo) {
        if (testInfo.getDisplayName().equals("Member 로그인 테스트") ||
            testInfo.getDisplayName().equals("Mercenary 로그인 테스트")) {
            ReflectionTestUtils.setField(
                    jwtConfig,
                    "secret",
                    "dGVzdF9qc29uX3dlYl90b2tlbl9zZWNyZXRfa2V5X2Zvcl9obWFj"
            );
        }
    }


    @DisplayName("Member 회원가입 및 정보 저장 테스트")
    @Test
    @Transactional
    void memberSignupTest() {
        LocalDate birth = LocalDate.of(1994, 3, 16);
        MemberSignUpRequest signup = new MemberSignUpRequest(
                "testMember",
                "01012341234",
                Gender.MALE,
                birth,
                "by0398467!@",
                1L,
                1L
        );
        City city = cityRepository.findById(1L).orElseThrow(
                () -> new NotFoundException(ErrorMessage.CITY_NOT_FOUND)
        );
        District district = districtRepository.findById(1L).orElseThrow(
                () -> new NotFoundException(ErrorMessage.DISTRICT_NOT_FOUND)
        );
        MemberEntity member = MemberEntity.create(signup, city, district);

        MemberSignupResponse response = authService.signupMember(signup);
        MemberEntity savedMember = memberRepository.findByMobile(signup.getMobile()).orElseThrow();
        int old = AgeUtil.calculate(member.getBirth());

        log.info("member name : " + member.getName() +
                 ", member old : " + AgeUtil.calculate(member.getBirth()) +
                 ", member mobile : " + member.getMobile());

        log.info("response name : " + response.getName() +
                 ", response old : " + response.getOld() +
                 ", response mobile : " + response.getMobile());

        log.info("savedMember name : " + savedMember.getName() +
                ", savedMember mobile : " + savedMember.getMobile());
        assertNotNull(savedMember);
        assertEquals(member.getName(), response.getName());
        assertEquals(member.getMobile(), response.getMobile());

        assertEquals(old, response.getOld());

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

    @DisplayName("Mercenary 등록 테스트")
    @Test
    @Transactional
    void mercenaryRegistrationTest() {
        MercenaryRegistrationRequest registration = new MercenaryRegistrationRequest(
                "testName",
                "01012341234",
                Gender.MALE,
                MercenaryRetentionStatus.KEEP
        );
        MercenaryEntity mercenary = MercenaryEntity.create(registration);
        MercenaryRegistrationResponse response = authService.registerMercenary(registration);
        MercenaryEntity savedMercenary = mercenaryRepository.findByMobile(registration.getMobile()).orElseThrow();

        log.info("mercenary name : " + mercenary.getName() +
                ", mercenary mobile : " + mercenary.getMobile());

        log.info("response name : " + response.getName() +
                ", response mobile : " + response.getMobile());
        assertNotNull(savedMercenary);
        assertEquals(mercenary.getName(), response.getName());
        assertEquals(mercenary.getMobile(), response.getMobile());
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

        LocalDate now = LocalDate.now();

        /** Create matchVote, match */
        MemberEntity admin = memberRepository.findByMobile("01043053451").get();

        MatchVoteCreateRequest matchVoteCreateRequest = new MatchVoteCreateRequest(
                MatchType.REGULAR, now
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
                        mercenary,
                        mercenaryCode,
                        match
                )
        );
        assertNotNull(transientEntity);
        assertNotNull(transientEntity.getCode());
        assertNotNull(transientEntity.getMatch());

        assertEquals(transientEntity.getMatch(), match);

        log.info("transientEntity matchId : {}", transientEntity.getMercenary().getId());
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


}
