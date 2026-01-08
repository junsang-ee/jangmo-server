package com.jangmo.web.domain.user;


import com.jangmo.web.constants.Gender;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.MemberEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@ActiveProfiles("test")
public class MemberDomainTest {

	@Test
	void 회원_도메인_생성_성공() {
		MemberSignUpRequest signUpRequest = new MemberSignUpRequest(
			"테스트맴버",
			"010-4305-3451",
			Gender.MALE,
			LocalDate.of(1999,9, 9),
			"test_password",
			1L,
			1L
		);
		City city = City.of("서울시");
		District district = District.of("강남구", city);

		MemberEntity member = MemberEntity.create(
			signUpRequest, city, district
		);
		log.info("member.getStatus :: {}", member.getStatus());
		assertNotNull(member);
	}
}
