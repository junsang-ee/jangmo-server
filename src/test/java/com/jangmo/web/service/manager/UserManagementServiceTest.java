package com.jangmo.web.service.manager;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.MercenaryRetentionStatus;
import com.jangmo.web.constants.user.MercenaryStatus;
import com.jangmo.web.constants.match.MatchType;
import com.jangmo.web.constants.vote.VoteSelectionType;
import com.jangmo.web.exception.NotFoundException;
import com.jangmo.web.model.dto.request.vote.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.dto.request.MercenaryRegistrationRequest;
import com.jangmo.web.model.dto.request.UserListSearchRequest;
import com.jangmo.web.model.dto.response.UserListResponse;
import com.jangmo.web.model.entity.MatchEntity;
import com.jangmo.web.model.entity.api.KakaoApiUsageEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.model.entity.user.MercenaryTransientEntity;

import com.jangmo.web.repository.*;

import com.jangmo.web.service.manager.UserManagementServiceImpl;
import com.jangmo.web.service.manager.VoteManagementServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RequiredArgsConstructor
@SpringBootTest
public class UserManagementServiceTest {

    @Autowired MemberRepository memberRepository;
    @Autowired MercenaryRepository mercenaryRepository;
    @Autowired CityRepository cityRepository;
    @Autowired DistrictRepository districtRepository;
    @Autowired MatchVoteRepository matchVoteRepository;
    @Autowired UserManagementServiceImpl userManagementService;
    @Autowired VoteManagementServiceImpl voteService;

    @Autowired KakaoApiUsageRepository kakaoApiUsageRepository;

    @DisplayName("Member 등록 승인 테스트")
    @Test
    @Transactional
    void memberApproveTest() {
        LocalDate birth = LocalDate.of(1994, 3, 16);
        MemberSignUpRequest signup = new MemberSignUpRequest(
                "testMember",
                "01012341234",
                Gender.MALE,
                birth,
                "1231231!",
                1L,
                1L
        );
        City city = cityRepository.findById(1L).orElseThrow();
        District district = districtRepository.findById(1L).orElseThrow();
        MemberEntity member = memberRepository.save(
                MemberEntity.create(signup, city, district)
        );

        log.info("Before approval member status : {}", member.getStatus());
        assertEquals(member.getStatus(), MemberStatus.PENDING);
        userManagementService.approveMember(member.getId());

        log.info("After approval member status : {}", member.getStatus());
        assertEquals(member.getStatus(), MemberStatus.ENABLED);
    }


    @DisplayName("Mercenary 등록 승인 테스트")
    @Test
    @Transactional
    void mercenaryApproveTest() {
        MercenaryRegistrationRequest registration = new MercenaryRegistrationRequest(
                "testName",
                "01012341234",
                Gender.MALE,
                MercenaryRetentionStatus.KEEP
        );

        MercenaryEntity mercenary = mercenaryRepository.save(
                MercenaryEntity.create(registration)
        );

        LocalDate now = LocalDate.now();
        LocalDate endAt = now.plusDays(1);
        LocalDate matchAt = now.plusDays(2);
        MatchVoteCreateRequest matchVoteCreateRequest = new MatchVoteCreateRequest(
                "testTitle",
                MatchType.FUTSAL,
                matchAt,
                endAt,
                VoteSelectionType.SINGLE
        );
        MemberEntity admin = memberRepository.findByMobile("01043053451").get();
        voteService.createMatchVote(admin.getId(), matchVoteCreateRequest);

        MatchVoteEntity matchVote = matchVoteRepository.findByMatchAt(matchAt).get(0);
        MatchEntity match = matchVote.getMatch();
        assertNotNull(match);
        assertEquals(match, matchVote.getMatch());
        log.info("Before approve mercenary status : {}", mercenary.getStatus());
        userManagementService.approveMercenary(mercenary.getId(), match.getId());

        MercenaryTransientEntity mercenaryTransient = mercenary.getMercenaryTransient();

        assertNotNull(mercenaryTransient);

        log.info("transientEntity code : {}", mercenaryTransient.getCode());

        assertEquals(mercenaryTransient.getMatch().getId(), match.getId());

        log.info("transientEntity matchId : {}", mercenaryTransient.getMatch().getId());
        log.info("original matchId : {}", match.getId());

        assertEquals(mercenary.getStatus(), MercenaryStatus.ENABLED);
        log.info("After approve mercenary status : {}", mercenary.getStatus());
    }

    @DisplayName("User(회원, 용병) 가입 및 등록 승인 요청 대기자 리스트 테스트")
    @Test
    void getApprovalUsersTest() {
        MemberSignUpRequest firstSignup = new MemberSignUpRequest(
                "testMember",
                "01012341111",
                Gender.MALE,
                LocalDate.of(1994, 3, 1),
                "1231231!",
                1L,
                1L
        );

        MemberSignUpRequest secondSignup = new MemberSignUpRequest(
                "testMember",
                "01012342222",
                Gender.MALE,
                LocalDate.of(1994, 3, 2),
                "1231231!",
                1L,
                1L
        );

        MemberSignUpRequest thirdSignup = new MemberSignUpRequest(
                "testMember",
                "01012343333",
                Gender.MALE,
                LocalDate.of(1994, 3, 3),
                "1231231!",
                1L,
                1L
        );
    }

    @DisplayName("관리자 및 본인 제외 전체 유저 리스트 테스트")
    @Transactional
    @Test
    void getUsersTest() {
        LocalDate birth = LocalDate.of(1994, 3, 16);
        MemberSignUpRequest firstSignupRequest = new MemberSignUpRequest(
                "firstTestMember",
                "01012341111",
                Gender.MALE,
                birth,
                "1231231!",
                1L,
                1L
        );
        MemberSignUpRequest secondSignupRequest = new MemberSignUpRequest(
                "secondTestMember",
                "01012342222",
                Gender.MALE,
                birth,
                "1231231!",
                1L,
                1L
        );
        MemberSignUpRequest thirdSignupRequest = new MemberSignUpRequest(
                "thirdTestMember",
                "01012343333",
                Gender.MALE,
                birth,
                "1231231!",
                1L,
                1L
        );
        MemberSignUpRequest fourthSignupRequest = new MemberSignUpRequest(
                "fourthTestMember",
                "01012344444",
                Gender.MALE,
                birth,
                "1231231!",
                1L,
                1L
        );
        MemberSignUpRequest fifthSignupRequest = new MemberSignUpRequest(
                "fifthTestMember",
                "01012345555",
                Gender.MALE,
                birth,
                "1231231!",
                1L,
                1L
        );
        MemberSignUpRequest sixthSignupRequest = new MemberSignUpRequest(
                "sixthTestMember",
                "01012346666",
                Gender.MALE,
                birth,
                "1231231!",
                1L,
                1L
        );
        City city = cityRepository.findById(1L).get();
        District district = districtRepository.findById(1L).get();
        MemberEntity firstMember = MemberEntity.create(firstSignupRequest, city, district);
        MemberEntity secondMember = MemberEntity.create(secondSignupRequest, city, district);
        MemberEntity thirdMember = MemberEntity.create(thirdSignupRequest, city, district);
        MemberEntity fourthMember = MemberEntity.create(fourthSignupRequest, city, district);
        MemberEntity fifthMember = MemberEntity.create(fifthSignupRequest, city, district);
        MemberEntity sixthMember = MemberEntity.create(sixthSignupRequest, city, district);
        List<MemberEntity> members = Arrays.asList(
                firstMember, secondMember, thirdMember,
                fourthMember, fifthMember, sixthMember
        );
        memberRepository.saveAll(members);

        MercenaryRegistrationRequest firstMercenaryRegistration = new MercenaryRegistrationRequest(
                "firstMercenary",
                "01012340001",
                Gender.MALE,
                MercenaryRetentionStatus.KEEP
        );
        MercenaryRegistrationRequest secondMercenaryRegistration = new MercenaryRegistrationRequest(
                "secondMercenary",
                "01012340002",
                Gender.MALE,
                MercenaryRetentionStatus.KEEP
        );
        MercenaryRegistrationRequest thirdMercenaryRegistration = new MercenaryRegistrationRequest(
                "thirdMercenary",
                "01012340003",
                Gender.MALE,
                MercenaryRetentionStatus.KEEP
        );
        MercenaryRegistrationRequest fourthMercenaryRegistration = new MercenaryRegistrationRequest(
                "fourthMercenary",
                "01012340004",
                Gender.MALE,
                MercenaryRetentionStatus.KEEP
        );
        MercenaryRegistrationRequest fifthMercenaryRegistration = new MercenaryRegistrationRequest(
                "fourthMercenary",
                "01012340005",
                Gender.MALE,
                MercenaryRetentionStatus.KEEP
        );
        MercenaryRegistrationRequest sixthMercenaryRegistration = new MercenaryRegistrationRequest(
                "fourthMercenary",
                "01012340006",
                Gender.MALE,
                MercenaryRetentionStatus.KEEP
        );

        UserEntity admin = memberRepository.findByMobile("01043053451").get();
        MercenaryEntity firstMercenary = MercenaryEntity.create(
                firstMercenaryRegistration
        );
        MercenaryEntity secondMercenary = MercenaryEntity.create(
                secondMercenaryRegistration
        );
        MercenaryEntity thirdMercenary = MercenaryEntity.create(
                thirdMercenaryRegistration
        );
        MercenaryEntity fourthMercenary = MercenaryEntity.create(
                fourthMercenaryRegistration
        );

        MercenaryEntity fifthMercenary = MercenaryEntity.create(
                fifthMercenaryRegistration
        );
        MercenaryEntity sixthMercenary = MercenaryEntity.create(
                sixthMercenaryRegistration
        );


        List<MercenaryEntity> mercenaryEntities = Arrays.asList(
                firstMercenary, secondMercenary, thirdMercenary,
                fourthMercenary, fifthMercenary, sixthMercenary
        );
        mercenaryRepository.saveAll(mercenaryEntities);
        Pageable pageable = PageRequest.of(0, 10);
        UserListSearchRequest searchRequest = new UserListSearchRequest();
        searchRequest.setRole(UserRole.MEMBER);
        searchRequest.setMemberStatus(MemberStatus.ENABLED);
        searchRequest.setMercenaryStatus(MercenaryStatus.ENABLED);
        searchRequest.setSearchKeyword(null);
        Page<UserListResponse> result = userManagementService.getUserList(
            admin.getId(),
            searchRequest,
            pageable
        );
        assertEquals(result.getContent().size(), 10);
        assertEquals(result.getTotalElements(), 12);
        assertEquals(result.getTotalPages(), 2);
        log.info("result list size : {}", result.getContent().size());
        log.info("result totalSize : {}", result.getTotalElements());
        log.info("result page size : {}", result.getTotalPages());

    }

    @DisplayName("회원 상태 변경 테스트")
    @Transactional
    @Test
    void updateMemberStatusTest() {
        City city = cityRepository.findById(1L).get();
        District district = districtRepository.findById(1L).get();
        LocalDate birth = LocalDate.of(1994, 3, 16);
        MemberSignUpRequest signup = new MemberSignUpRequest(
                "firstTestMember",
                "01012341111",
                Gender.MALE,
                birth,
                "1231231!",
                1L,
                1L
        );

        MemberEntity member = MemberEntity.create(
                signup, city, district
        );

        memberRepository.save(member);
        assertEquals(MemberStatus.PENDING, member.getStatus());
        log.info("Before update status : {}", member.getStatus());

        String memberId = member.getId();
        MemberStatus status = MemberStatus.ENABLED;
        userManagementService.updateMemberStatus(memberId, status);

        assertEquals(MemberStatus.ENABLED, member.getStatus());
        log.info("After update status : {}", member.getStatus());
    }


    @DisplayName("용병 상태 변경 테스트")
    @Transactional
    @Test
    void updateMercenaryStatusTest() {
        MercenaryRegistrationRequest registration = new MercenaryRegistrationRequest(
                "김용병",
                "01012340001",
                Gender.MALE,
                MercenaryRetentionStatus.KEEP
        );

        MercenaryEntity mercenary = MercenaryEntity.create(registration);
        mercenaryRepository.save(mercenary);
        assertEquals(MercenaryStatus.PENDING, mercenary.getStatus());
        log.info("Before update status : {}", mercenary.getStatus());

        String mercenaryId = mercenary.getId();
        MercenaryStatus status = MercenaryStatus.ENABLED;
        userManagementService.updateMercenaryStatus(mercenaryId, status);

        assertEquals(MercenaryStatus.ENABLED, mercenary.getStatus());
        log.info("After update status : {}", mercenary.getStatus());
    }

    @DisplayName("회원 권한(role) 변경 테스트 및 Kakao 사용량 데이터 생성 테스트")
    @Transactional
    @Test
    void updateMemberRoleTest() {
        City city = cityRepository.findById(1L).get();
        District district = districtRepository.findById(1L).get();
        LocalDate birth = LocalDate.of(1994, 3, 16);
        MemberSignUpRequest signup = new MemberSignUpRequest(
                "firstTestMember",
                "01012341111",
                Gender.MALE,
                birth,
                "1231231!",
                1L,
                1L
        );
        MemberEntity member = MemberEntity.create(signup, city, district);
        memberRepository.save(member);
        assertNotNull(memberRepository.findByMobile("01012341111"));
        KakaoApiUsageEntity beforeKakaoApiUsage = kakaoApiUsageRepository.findByApiCaller(member).orElseGet(
                () -> null
        );
        assertNull(beforeKakaoApiUsage);
        userManagementService.updateMemberRole(member.getId(), UserRole.MANAGER);
        KakaoApiUsageEntity afterKakaoApiUsage = kakaoApiUsageRepository.findByApiCaller(member).orElseGet(
                () -> null
        );
        assertNotNull(afterKakaoApiUsage);
        log.info("afterKakaoApiUsage id : {}", afterKakaoApiUsage.getId());
        log.info("afterKakaoApiUsage apiCallerName : {}", afterKakaoApiUsage.getApiCaller().getName());
    }



}
