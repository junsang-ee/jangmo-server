package com.jangmo.web.service.manager;

import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.match.MatchType;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.vote.VoteSelectionType;
import com.jangmo.web.model.dto.request.vote.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.response.vote.MatchVoteCreateResponse;
import com.jangmo.web.model.entity.MatchEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import com.jangmo.web.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
public class VoteManagementServiceTest {

    @Autowired VoteManagementServiceImpl voteService;

    @Autowired UserRepository userRepository;

    @Autowired MemberRepository memberRepository;

    @Autowired VoteRepository voteRepository;

    @Autowired MatchRepository matchRepository;

    static final String MATCH_VOTE_CREATE = "매치 투표(MatchVote 생성 테스트";

    @Value("${jangmo.admin.mobile}")
    String adminMobile;

    MemberEntity admin = null;

    @BeforeEach
    void init(TestInfo testInfo) {
        String display = testInfo.getDisplayName();
        switch (display) {
            case MATCH_VOTE_CREATE:
                initAdmin();
                break;
            default: break;
        }
    }

    void initAdmin() {
        admin = memberRepository.findByMobile(adminMobile).orElseGet(
                () -> null
        );
        assertNotNull(admin);
    }

    @DisplayName(MATCH_VOTE_CREATE)
    @Test
    @Transactional
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
        MatchVoteCreateResponse response = voteService.createMatchVote(admin.getId(), createRequest);
        List<MatchVoteEntity> matchVoteList = voteRepository.findByMatchAt(matchAt);
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

        log.info("======================================================");
        log.info("matchVote getMatchId : {}", matchVote.getMatch().getId());
        log.info("matchId : {}", match.getId());
        log.info("======================================================");
        log.info("matchVote matchAt : {}", matchVote.getMatchAt());
        log.info("matchAt : {}", matchAt);
        log.info("======================================================");
        log.info("matchVote voteEndAt : {}", matchVote.getEndAt());
        log.info("voteEndAt : {}", voteEndAt);
        log.info("======================================================");
        log.info("matchVote createdByName : {}", matchVote.getCreatedBy().getName());
        log.info("admin name : {}", admin.getName());
        log.info("======================================================");

        int voterCount = userRepository.findUserByMemberStatusAndRoleNot(
                MemberStatus.ENABLED,
                UserRole.MERCENARY
        ).size();
        log.info("voterCount : {}", voterCount);
        assertEquals(voterCount, matchVote.getVoters().size());
    }
}
