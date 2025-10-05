package com.jangmo.web.service.manager;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.match.MatchType;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.vote.VoteSelectionType;
import com.jangmo.web.constants.vote.VoteType;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.dto.request.vote.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.request.vote.VoteListRequest;
import com.jangmo.web.model.dto.response.vote.MatchVoteCreateResponse;
import com.jangmo.web.model.dto.response.vote.VoteListResponse;
import com.jangmo.web.model.entity.MatchEntity;
import com.jangmo.web.model.entity.administrative.City;
import com.jangmo.web.model.entity.administrative.District;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import com.jangmo.web.repository.*;

import com.jangmo.web.repository.user.MemberRepository;
import com.jangmo.web.repository.user.UserRepository;
import com.jangmo.web.repository.vote.MatchVoteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class VoteManagementServiceTest {

    @Autowired VoteManagementServiceImpl voteService;
    @Autowired UserRepository userRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MatchVoteRepository matchVoteRepository;
    @Autowired MatchRepository matchRepository;
    @Autowired CityRepository cityRepository;
    @Autowired DistrictRepository districtRepository;

    static final String MATCH_VOTE_CREATE = "매치 투표(MatchVote) 생성 테스트";
    static final String VOTE_LIST = "투표(Votes) 리스트 테스트";

    @Value("${jangmo.admin.name}")
    String adminName;

    @Value("${jangmo.admin.password}")
    String adminPassword;

    @Value("${jangmo.admin.mobile}")
    String adminMobile;

    MemberEntity admin = null;

    @Autowired
    private VoteManagementServiceImpl voteManagementService;

    @BeforeEach
    void init(TestInfo testInfo) {
        createCityAndDistrict();
        createAdmin();
        if (testInfo.getDisplayName().equals(VOTE_LIST)) {
            createUsers();
        }
    }

    void createCityAndDistrict() {
        City city = City.of("서울특별시");
        District district = District.of("종로구", city);
        cityRepository.save(city);
        districtRepository.save(district);
    }

    void createAdmin() {
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest(
           adminName,
           adminMobile,
           Gender.MALE,
           LocalDate.of(1994, 3, 16),
           adminPassword,
           1L,
           1L
        );
        City city = cityRepository.findByName("서울특별시")
                .orElse(null);

        District district = districtRepository.findByCityAndName(city, "종로구")
                .orElse(null);

        assertNotNull(city);
        assertNotNull(district);

        admin = memberRepository.save(
                MemberEntity.create(
                        memberSignUpRequest, city, district
                )
        );
        assertNotNull(admin);
    }

    void createUsers() {
        LocalDate birth = LocalDate.of(2020, 2 ,20);
        String password = "1231231!";
        MemberSignUpRequest firstMemberCreateRequest = new MemberSignUpRequest(
                "김철수",
                "01012341111",
                Gender.MALE,
                birth,
                password,
                1L,
                1L
        );
        MemberSignUpRequest secondMemberCreateRequest = new MemberSignUpRequest(
                "김수빈",
                "01012342222",
                Gender.MALE,
                birth,
                password,
                1L,
                1L
        );
        MemberSignUpRequest thirdMemberCreateRequest = new MemberSignUpRequest(
                "김희철",
                "01012343333",
                Gender.MALE,
                birth,
                password,
                1L,
                1L
        );
        City city = City.of("서울특별시");
        District district = District.of("관악구", city);
        MemberEntity firstMember = MemberEntity.create(
                firstMemberCreateRequest, city, district
        );
        MemberEntity secondMember = MemberEntity.create(
                secondMemberCreateRequest, city, district
        );
        MemberEntity thirdMember = MemberEntity.create(
                thirdMemberCreateRequest, city, district
        );
        firstMember.updateStatus(MemberStatus.ENABLED);
        secondMember.updateStatus(MemberStatus.ENABLED);
        thirdMember.updateStatus(MemberStatus.ENABLED);
    }

    @DisplayName(MATCH_VOTE_CREATE)
    @Test
    void matchVoteCreateTest() {
        LocalDate now = LocalDate.now();
        LocalDate matchAt = now.plusDays(2);
        LocalDate voteEndAt = now.plusDays(1);
        MatchVoteCreateRequest createRequest = new MatchVoteCreateRequest(
                "testTitle",
                MatchType.FUTSAL,
                matchAt,
                voteEndAt,
                VoteSelectionType.SINGLE
        );
        MatchVoteCreateResponse response = voteService.createMatchVote(
                admin.getId(), createRequest
        );

        List<MatchVoteEntity> matchVoteList = matchVoteRepository.findByMatchAt(matchAt);
        log.info("matchVoteList size: " + matchVoteList.size());
        MatchVoteEntity matchVote = matchVoteList.get(0);

        MatchEntity match = matchRepository.findByMatchVote(matchVote).orElseGet(() -> null);
        assertNotNull(response);
        assertNotNull(matchVote);
        assertNotNull(match);

        assertEquals(matchVote.getMatch().getId(), match.getId());
        assertEquals(matchVote.getMatchAt(), matchAt);
        assertEquals(matchVote.getEndAt(), voteEndAt);
        assertEquals(matchVote.getCreatedBy().getName(), admin.getName());

        int voterCount = userRepository.findUserByMemberStatusAndRoleNot(
                MemberStatus.ENABLED, UserRole.MERCENARY
        ).size();

        log.info("voterCount : {}", voterCount);
        assertEquals(voterCount, matchVote.getVoters().size());
    }

    @DisplayName(VOTE_LIST)
    @Test
    void voteListTest() {
        LocalDate now = LocalDate.now();
        MatchVoteCreateRequest firstMatchVoteCreateRequest = new MatchVoteCreateRequest(
                "firstTitle",
                MatchType.FUTSAL,
                now.plusDays(2), // matchAt
                now.plusDays(1), // voteEndAt
                VoteSelectionType.SINGLE
        );

        MatchVoteCreateRequest secondMatchVoteCreateRequest = new MatchVoteCreateRequest(
                "secondTitle",
                MatchType.FUTSAL,
                now.plusDays(1), // matchAt
                now,                       // voteEndAt
                VoteSelectionType.SINGLE
        );

        List<UserEntity> voters = userRepository.findUserByMemberStatusAndRoleNot(
                MemberStatus.ENABLED, UserRole.MERCENARY
        );

        MatchVoteEntity firstMatchVote = MatchVoteEntity.create(
                admin, firstMatchVoteCreateRequest, voters
        );

        MatchVoteEntity secondMatchVote = MatchVoteEntity.create(
                admin, secondMatchVoteCreateRequest, voters
        );

        matchVoteRepository.save(firstMatchVote);
        matchVoteRepository.save(secondMatchVote);

        VoteListRequest voteListRequest = new VoteListRequest(
                now.getYear(),
                now.getMonthValue(),
                VoteType.MATCH
        );
        List<VoteListResponse> votes = voteManagementService.getVotes(voteListRequest);

        log.info("votes size : {}", votes.size());
        assertEquals(2, votes.size());
    }
}

