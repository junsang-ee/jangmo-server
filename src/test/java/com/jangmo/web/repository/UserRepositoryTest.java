package com.jangmo.web.repository;

import com.jangmo.web.constants.*;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.dto.request.MercenaryRegistrationRequest;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class UserRepositoryTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private MercenaryRepository mercenaryRepository;
    @Autowired private UserRepository userRepository;

    @Test
    void findByRoleNotAndStatusTest() {
        MemberSignUpRequest testFirstMemberRequest = new MemberSignUpRequest(
                "testFirstMember",
                "01012340001",
                MobileCarrierType.KT,
                Gender.MALE,
                19940101,
                "testPassword",
                "testAddress",
                UserRole.ADMIN
        );

        MemberSignUpRequest testSecondMemberRequest = new MemberSignUpRequest(
                "testSecondMember",
                "01012340002",
                MobileCarrierType.KT,
                Gender.MALE,
                19940102,
                "testPassword",
                "testAddress",
                UserRole.MEMBER
        );

        MemberSignUpRequest testThirdMemberRequest = new MemberSignUpRequest(
                "testThirdMember",
                "01012340003",
                MobileCarrierType.KT,
                Gender.MALE,
                19940103,
                "testPassword",
                "testAddress",
                UserRole.MEMBER
        );

        MemberEntity testFirstMember = MemberEntity.create(testFirstMemberRequest);
        MemberEntity testSecondMember = MemberEntity.create(testSecondMemberRequest);
        MemberEntity testThirdMember = MemberEntity.create(testThirdMemberRequest);

        MercenaryRegistrationRequest testFirstMercenaryRequest = new MercenaryRegistrationRequest(
                "testFirstMercenary",
                "01012340004",
                MobileCarrierType.KT,
                UserRole.MERCENARY,
                Gender.MALE,
                MercenaryRetentionStatus.KEEP
        );

        MercenaryRegistrationRequest testSecondMercenaryRequest = new MercenaryRegistrationRequest(
                "testSecondMercenary",
                "01012340005",
                MobileCarrierType.KT,
                UserRole.MERCENARY,
                Gender.MALE,
                MercenaryRetentionStatus.KEEP
        );

        MercenaryEntity testFirstMercenary = MercenaryEntity.create(testFirstMercenaryRequest);
        MercenaryEntity testSecondMercenary = MercenaryEntity.create(testSecondMercenaryRequest);

        testFirstMember.updateStatus(UserStatus.ENABLED);
        testSecondMember.updateStatus(UserStatus.ENABLED);
        testFirstMercenary.updateStatus(UserStatus.ENABLED);

        List<MemberEntity> testMemberList = Arrays.asList(
                testFirstMember, testSecondMember, testThirdMember
        );

        List<MercenaryEntity> testMercenaryList = Arrays.asList(
                testFirstMercenary, testSecondMercenary
        );

        memberRepository.saveAll(testMemberList);
        mercenaryRepository.saveAll(testMercenaryList);

        List<UserEntity> enabledUsers = userRepository.findByRoleNotAndStatus(UserRole.MERCENARY, UserStatus.ENABLED);
        assertThat(enabledUsers).hasSize(2);
        assertThat(enabledUsers.get(0).getRole()).isNotEqualTo(UserRole.MERCENARY);
        assertThat(enabledUsers.get(0).getStatus()).isEqualTo(UserStatus.ENABLED);
        assertThat(enabledUsers.get(1).getRole()).isNotEqualTo(UserRole.MERCENARY);
        assertThat(enabledUsers.get(1).getStatus()).isEqualTo(UserStatus.ENABLED);


    }
}
