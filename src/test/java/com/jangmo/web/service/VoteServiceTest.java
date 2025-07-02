package com.jangmo.web.service;

import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.match.MatchType;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.exception.NotFoundException;
import com.jangmo.web.model.dto.request.vote.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.response.vote.MatchVoteCreateResponse;
import com.jangmo.web.model.entity.MatchEntity;
import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.repository.MatchRepository;
import com.jangmo.web.repository.MatchVoteRepository;
import com.jangmo.web.repository.UserRepository;
import com.jangmo.web.service.manager.VoteServiceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
public class VoteServiceTest {

    @Autowired VoteServiceImpl voteService;

    @Autowired UserRepository userRepository;

    @Autowired MatchVoteRepository matchVoteRepository;

    @Autowired MatchRepository matchRepository;


    @DisplayName("match 투표 생성 테스트")
    @Test
    @Transactional
    void matchVoteCreateTest() {
        UserEntity admin = userRepository.findByMobile("01043053451").orElseThrow(
                () -> new NotFoundException(ErrorMessage.USER_NOT_FOUND)
        );
        LocalDate now = LocalDate.now();
        LocalDate matchAt = now.plusDays(2);
        LocalDate voteEndAt = now.plusDays(1);
        MatchVoteCreateRequest createRequest = new MatchVoteCreateRequest(
                "testTitle",
                MatchType.FUTSAL,
                matchAt,
                voteEndAt
        );
        MatchVoteCreateResponse response = voteService.createMatchVote(admin.getId(), createRequest);
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
