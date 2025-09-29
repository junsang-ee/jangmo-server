package com.jangmo.web.repository;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.MercenaryRetentionStatus;
import com.jangmo.web.constants.UserRole;

import com.jangmo.web.constants.user.MercenaryStatus;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.dto.request.MercenaryRegistrationRequest;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.repository.user.MemberRepository;
import com.jangmo.web.repository.user.MercenaryRepository;
import com.jangmo.web.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Sql("/test_sql/area_city_test.sql")
@Sql("/test_sql/area_district_test.sql")
@Slf4j
@DataJpaTest
public class UserRepositoryTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private MercenaryRepository mercenaryRepository;
    @Autowired private UserRepository userRepository;

    @Autowired private CityRepository cityRepository;

    @Autowired private DistrictRepository districtRepository;

    @Test
    void findUserByMemberStatusAndRoleNotTest() {
        LocalDate birth = LocalDate.of(1994, 3, 16);
        MemberSignUpRequest testFirstMemberRequest = new MemberSignUpRequest(
                "testFirstMember",
                "01012340001",
                Gender.MALE,
                birth,
                "testPassword1",
                1L,
                1L
        );

        MemberSignUpRequest testSecondMemberRequest = new MemberSignUpRequest(
                "testSecondMember",
                "01012340002",
                Gender.MALE,
                birth,
                "testPassword2",
                1L,
                1L
        );

        MemberSignUpRequest testThirdMemberRequest = new MemberSignUpRequest(
                "testThridMember",
                "01012340003",
                Gender.MALE,
                birth,
                "testPassword3",
                1L,
                1L
        );
        City city = cityRepository.findById(1L).get();
        District district = districtRepository.findById(1L).get();
        MemberEntity testFirstMember = MemberEntity.create(testFirstMemberRequest, city, district);
        MemberEntity testSecondMember = MemberEntity.create(testSecondMemberRequest, city, district);
        MemberEntity testThirdMember = MemberEntity.create(testThirdMemberRequest, city, district);

        MercenaryRegistrationRequest testFirstMercenaryRequest = new MercenaryRegistrationRequest(
                "testFirstMercenary",
                "01012340004",
                Gender.MALE,
                MercenaryRetentionStatus.KEEP
        );

        MercenaryRegistrationRequest testSecondMercenaryRequest = new MercenaryRegistrationRequest(
                "testSecondMercenary",
                "01012340005",
                Gender.MALE,
                MercenaryRetentionStatus.KEEP
        );

        MercenaryEntity testFirstMercenary = MercenaryEntity.create(testFirstMercenaryRequest);
        MercenaryEntity testSecondMercenary = MercenaryEntity.create(testSecondMercenaryRequest);

        testFirstMember.updateStatus(MemberStatus.ENABLED);
        testSecondMember.updateStatus(MemberStatus.ENABLED);
        testFirstMercenary.updateStatus(MercenaryStatus.ENABLED);

        List<MemberEntity> testMemberList = Arrays.asList(
                testFirstMember, testSecondMember, testThirdMember
        );

        List<MercenaryEntity> testMercenaryList = Arrays.asList(
                testFirstMercenary, testSecondMercenary
        );

        memberRepository.saveAll(testMemberList);
        mercenaryRepository.saveAll(testMercenaryList);
        List<MemberEntity> members = memberRepository.findAll();
        List<MercenaryEntity> mercenaries = mercenaryRepository.findAll();
        for (MemberEntity member : members) {
            log.info("member name : " + member.getName() + ", member status : " + member.getStatus());
        }

        for (MercenaryEntity mercenary : mercenaries) {
            log.info("mercenary name : " + mercenary.getName() + ", mercenary status : " + mercenary.getStatus());
        }

        List<UserEntity> enabledUsers = userRepository.findUserByMemberStatusAndRoleNot(MemberStatus.ENABLED, UserRole.MERCENARY);
        assertThat(enabledUsers).hasSize(2);
        assertThat(enabledUsers.get(0).getRole()).isNotEqualTo(UserRole.MERCENARY);
        assertThat(enabledUsers.get(1).getRole()).isNotEqualTo(UserRole.MERCENARY);
        assertThat(enabledUsers.contains(testFirstMember)).isEqualTo(true);
        assertThat(enabledUsers.contains(testSecondMember)).isEqualTo(true);
        assertThat(enabledUsers.contains(testThirdMember)).isNotEqualTo(true);
        assertThat(enabledUsers.contains(testFirstMercenary)).isNotEqualTo(true);
        assertThat(enabledUsers.contains(testSecondMercenary)).isNotEqualTo(true);

    }
}
