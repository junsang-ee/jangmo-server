package com.jangmo.web.service;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MemberStatus;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.repository.MemberRepository;
import com.jangmo.web.utils.EncryptUtil;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
public class UserServiceTest {

    @Autowired AuthServiceImpl authService;
    @Autowired UserServiceImpl userService;
    @Autowired MemberRepository memberRepository;

    @DisplayName("User(Member 회원) 비밀번호 변경 테스트")
    @Test
    @Transactional
    void memberUpdatePasswordTest() {

        MemberSignUpRequest signUpRequest = new MemberSignUpRequest(
                "황테스트",
                "01012341234",
                Gender.MALE,
                LocalDate.of(1994, 3, 17),
                "oldPassword!@",
                1L,
                1L
        );
        authService.signupMember(signUpRequest);
        MemberEntity member = memberRepository.findByMobile("01012341234").orElseGet(() -> null);
        assertNotNull(member);
        member.updateStatus(MemberStatus.ENABLED);
        assertEquals(member.getStatus(), MemberStatus.ENABLED);

        userService.updatePassword(member.getId(), "oldPassword!@", "newPassword!@");
        assertTrue(EncryptUtil.matches("newPassword!@", member.getPassword()));
    }
}
