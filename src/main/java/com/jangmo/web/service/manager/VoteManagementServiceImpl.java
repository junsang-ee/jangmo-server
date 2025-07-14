package com.jangmo.web.service.manager;

import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.NotFoundException;
import com.jangmo.web.model.dto.request.vote.GeneralVoteCreateRequest;
import com.jangmo.web.model.dto.request.vote.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.response.vote.GeneralVoteCreateResponse;
import com.jangmo.web.model.dto.response.vote.MatchVoteCreateResponse;
import com.jangmo.web.model.entity.vote.GeneralVoteEntity;
import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.repository.GeneralVoteRepository;
import com.jangmo.web.repository.MatchVoteRepository;
import com.jangmo.web.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoteManagementServiceImpl implements VoteManagementService {

    private final MatchVoteRepository matchVoteRepository;

    private final GeneralVoteRepository generalVoteRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public MatchVoteCreateResponse createMatchVote(String userId, MatchVoteCreateRequest request) {
        UserEntity createdBy = getUserById(userId);
        List<UserEntity> rawVoters = getRawMatchVoters();
        MatchVoteEntity matchVote = MatchVoteEntity.create(
                createdBy, request, rawVoters
        );
        matchVoteRepository.save(matchVote);
        return MatchVoteCreateResponse.of(
                matchVote.getStartAt(),
                matchVote.getEndAt(),
                matchVote.getMatchAt(),
                matchVote.getCreatedBy().getName()
        );
    }

    @Override
    @Transactional
    public GeneralVoteCreateResponse createGeneralVote(String userId, GeneralVoteCreateRequest request) {
        UserEntity createdBy = getUserById(userId);
        List<UserEntity> rawVoters = getRawGeneralVoters();
        GeneralVoteEntity generalVote = GeneralVoteEntity.create(
                createdBy, request, rawVoters
        );
        generalVoteRepository.save(generalVote);
        return GeneralVoteCreateResponse.of(
                generalVote.getStartAt(),
                generalVote.getEndAt(),
                generalVote.getCreatedBy().getName()
        );
    }

    private List<UserEntity> getRawGeneralVoters() {
        List<MemberStatus> statuses = Arrays.asList(
                MemberStatus.DISABLED, MemberStatus.PENDING
        );
        return userRepository.findUserByMemberStatusNotAndRole(
                statuses, UserRole.MEMBER
        );
    }


    private UserEntity getUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.USER_NOT_FOUND)
        );
    }

    private List<UserEntity> getRawMatchVoters() {
        return userRepository.findUserByMemberStatusAndRoleNot(
                MemberStatus.ENABLED,
                UserRole.MERCENARY
        );
    }

}
