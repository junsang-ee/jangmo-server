package com.jangmo.web.service;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MemberStatus;
import com.jangmo.web.model.dto.request.MemberLoginRequest;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.dto.response.MemberSignupResponse;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.common.BaseUuidEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.repository.CityRepository;
import com.jangmo.web.repository.DistrictRepository;
import com.jangmo.web.repository.MemberRepository;
import com.jangmo.web.utils.EncryptUtil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.Assertions;


import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.any;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;



@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private DistrictRepository districtRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void init(TestInfo testInfo) {
        if (testInfo.getDisplayName().equals("Member 회원가입 및 정보 저장 테스트")) {
            memberRepository.deleteAll();
        }
    }

    @DisplayName("Member 회원가입 및 정보 저장 테스트")
    @Test
    @Transactional
    void memberSignupTest() {
        LocalDate birth = LocalDate.of(1994, 3, 16);
        City city = City.of("서울특별시");
        District district = District.of("종로구", city);
        MemberSignUpRequest signup = new MemberSignUpRequest(
                "testMember",
                "01043053451",
                Gender.MALE,
                birth,
                "by0398467!@",
                1L,
                1L
        );

        given(cityRepository.findById(1L)).willReturn(Optional.of(city));
        given(districtRepository.findById(1L)).willReturn(Optional.of(district));

        MemberEntity mockMember = MemberEntity.create(signup, city, district);
        MemberSignupResponse mockResponse = MemberSignupResponse.of(mockMember);

        given(memberRepository.save(any(MemberEntity.class))).willReturn(mockMember);

        MemberSignupResponse response = authService.signupMember(signup);

        Assertions.assertEquals(mockResponse.getName(), response.getName());
        Assertions.assertEquals(mockResponse.getMobile(), response.getMobile());
        verify(memberRepository, times(1)).save(any(MemberEntity.class));
    }

    @DisplayName("Member 로그인 테스트")
    @Test
    void memberLoginTest() {
        LocalDate birth = LocalDate.of(1994, 3, 16);
        City city = City.of("서울특별시");
        District district = District.of("종로구", city);
        String rawPassword = "rawPassword";
        MemberSignUpRequest signup = new MemberSignUpRequest(
                "testMember",
                "01043053451",
                Gender.MALE,
                birth,
                rawPassword,
                1L,
                1L
        );

        MemberEntity mockMember = MemberEntity.create(signup, city, district);
        try {
            Field idField = BaseUuidEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(mockMember, "a2ef3d73-27d9-480d-b245-39f518c142fe");
        } catch (Exception ignored) {
        }

        mockMember.updateStatus(MemberStatus.ENABLED);

        try (MockedStatic<EncryptUtil> mockedStatic = Mockito.mockStatic(EncryptUtil.class)) {
            mockedStatic.when(
                    () -> EncryptUtil.matches(rawPassword, mockMember.getPassword())
            ).thenReturn(true);

            /**
             * @TestSecretKey
             * */
            String secret = "dGVzdF9qc29uX3dlYl90b2tlbl9zZWNyZXRfa2V5X2Zvcl9obWFj";
            SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

            given(memberRepository.findByMobile("01043053451")).willReturn(Optional.of(mockMember));

            String userAgent = "Mozilla/5.0 ...";
            MemberLoginRequest loginRequest = new MemberLoginRequest("01043053451", rawPassword);

            String token = authService.loginMember(userAgent, loginRequest);

            assertNotNull(token);

            log.info("token : " + token);

            String userId = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();

            log.info("userId : " + userId);

            Assertions.assertEquals("a2ef3d73-27d9-480d-b245-39f518c142fe", userId);

            verify(memberRepository, times(1)).findByMobile("01043053451");

            /**
             * Test, Dependency Issue :: AuthServiceImpl -> createJwt(); method 임의로 생성
             * loginMember() -> 반환 메소드 임의 변경 jwtProvider.create() -> createJwt()
             * Copy jwtProvider.create() method (AuthServiceImpl 에 임의로 변경 후 테스트)
             *     private String createJwt(String userAgent, String id, UserRole role) {
             *         SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode("{key}"));
             *         Date issuedAt = new Date();
             *         Date expireAt = new Date(issuedAt.getTime() + Duration.ofHours(2).toMillis());
             *         return Jwts.builder()
             *                 .subject(id)
             *                 .claim("role", role.name())
             *                 .claim("agent", userAgent)
             *                 .issuedAt(issuedAt)
             *                 .expiration(expireAt)
             *                 .signWith(secretKey, Jwts.SIG.HS256)
             *                 .compact();
             *     }
             *
             */
        }
    }

}
