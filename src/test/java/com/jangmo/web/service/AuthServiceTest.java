package com.jangmo.web.service;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.MobileCarrierType;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.model.dto.request.MemberSignupRequest;
import com.jangmo.web.model.entity.MemberEntity;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.repository.CityRepository;
import com.jangmo.web.repository.MemberRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.*;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void init(TestInfo testInfo) {
        if (testInfo.getDisplayName().equals("Member 회원가입 및 정보 저장")) {
            memberRepository.deleteAll();
        }
    }


    @DisplayName("Member 회원가입 및 정보 저장")
    @Test
    @Transactional
    void memberSignupTest() {
        MemberSignupRequest signup = MemberSignupRequest.builder()
                .name("testMember")
                .role(UserRole.MEMBER)
                .birth(19940316)
                .gender(Gender.MALE)
                .mobileCarrier(MobileCarrierType.KT)
                .address("서울시 관악구")
                .phoneNumber(01043053451)
                .password("testPassword")
                .build();

        MemberEntity mockMember = MemberEntity.create(signup);

        given(memberRepository.save(any(MemberEntity.class))).willReturn(mockMember);

        MemberEntity member = authService.signup(signup);

        Assertions.assertEquals(mockMember.getName(), member.getName());
        verify(memberRepository, times(1)).save(any(MemberEntity.class));
    }
}
