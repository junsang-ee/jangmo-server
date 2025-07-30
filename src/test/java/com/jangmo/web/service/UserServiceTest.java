package com.jangmo.web.service;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.exception.NotFoundException;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.entity.UniformEntity;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.repository.CityRepository;
import com.jangmo.web.repository.DistrictRepository;
import com.jangmo.web.repository.MemberRepository;
import com.jangmo.web.utils.EncryptUtil;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@RequiredArgsConstructor
@SpringBootTest
public class UserServiceTest {

    private final  UserServiceImpl userService;
    private final  MemberRepository memberRepository;
    private final  CityRepository cityRepository;
    private final  DistrictRepository districtRepository;

    private MemberEntity testMember;

    @BeforeEach
    void setUpMember(TestInfo testInfo) {
        String displayName = testInfo.getDisplayName();
        if (displayName.equals("회원(Member) 비밀번호 변경 테스트") ||
            displayName.equals("회원(Member) 유니폼 등록 테스트") ||
            displayName.equals("회원(Member) 유니폼 등번호 변경 테스트")) {
            testMember = createTestMember();
        }
    }

    private MemberEntity createTestMember() {
        City city = cityRepository.findById(1L).orElseThrow(
                () -> new NotFoundException(ErrorMessage.CITY_NOT_FOUND)
        );
        District district = districtRepository.findById(1L).orElseThrow(
                () -> new NotFoundException(ErrorMessage.DISTRICT_NOT_FOUND)
        );
        MemberSignUpRequest signUpRequest = new MemberSignUpRequest(
                "김회원",
                "01012341234",
                Gender.MALE,
                LocalDate.of(1994, 3, 17),
                "oldPassword!@",
                1L,
                1L
        );
        MemberEntity member = MemberEntity.create(signUpRequest, city, district);
        memberRepository.save(member);
        assertNotNull(member);
        return member;
    }

    @DisplayName("회원(Member) 비밀번호 변경 테스트")
    @Test
    @Transactional
    void memberUpdatePasswordTest() {
        testMember.updateStatus(MemberStatus.ENABLED);
        assertEquals(testMember.getStatus(), MemberStatus.ENABLED);

        userService.updatePassword(testMember.getId(), "oldPassword!@", "newPassword!@");
        assertTrue(EncryptUtil.matches("newPassword!@", testMember.getPassword()));
    }

    @DisplayName("회원(Member) 유니폼 등록 테스트")
    @Test
    @Transactional
    void registerUniformInfoTest() {
        testMember.registerUniform(11);
        UniformEntity uniform = testMember.getUniform();
        assertNotNull(uniform);
        log.info("uniform ID :: {}", uniform.getId());
        log.info("uniform backNumber :: {}", uniform.getBackNumber());
    }

    @DisplayName("회원(Member) 유니폼 등번호 변경 테스트")
    @Test
    @Transactional
    public void updateBackNumberTest() {
        testMember.registerUniform(10);
        UniformEntity uniform = testMember.getUniform();
        assertNotNull(uniform);
        log.info("register uniform backNumber : {}", uniform.getBackNumber());
        uniform.updateBackNumber(99);
        assertEquals(99, uniform.getBackNumber());
        log.info("update uniform backNumber : {}", uniform.getBackNumber());
    }


}
