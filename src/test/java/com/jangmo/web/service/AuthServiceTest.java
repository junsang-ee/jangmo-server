package com.jangmo.web.service;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.repository.CityRepository;
import com.jangmo.web.repository.DistrictRepository;
import com.jangmo.web.repository.MemberRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

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

    @Mock
    private CityRepository cityRepository;

    @Mock
    private DistrictRepository districtRepository;


    @BeforeEach
    void init(TestInfo testInfo) {
        if (testInfo.getDisplayName().equals("Member 회원가입 및 정보 저장 테스트")) {
            memberRepository.deleteAll();
        } else if (testInfo.getDisplayName().equals("Member 로그인 테스트")) {
            memberSignupTest();
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

        given(memberRepository.save(any(MemberEntity.class))).willReturn(mockMember);

        MemberEntity member = authService.signupMember(signup);

        Assertions.assertEquals(mockMember.getName(), member.getName());
        verify(memberRepository, times(1)).save(any(MemberEntity.class));
    }


}
