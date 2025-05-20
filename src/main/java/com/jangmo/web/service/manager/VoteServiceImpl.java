package com.jangmo.web.service.manager;

import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.NotFoundException;
import com.jangmo.web.model.dto.request.MatchVoteCreateRequest;
import com.jangmo.web.model.dto.response.MatchVoteCreateResponse;
import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import com.jangmo.web.model.entity.MatchVoteUserEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.repository.MatchVoteRepository;
import com.jangmo.web.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoteServiceImpl implements VoteService {

    private final MatchVoteRepository matchVoteRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public MatchVoteCreateResponse createMatchVote(String userId, MatchVoteCreateRequest request) {
        UserEntity createdBy = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.USER_NOT_FOUND)
        );
        MatchVoteEntity matchVote = MatchVoteEntity.create(createdBy, request);
        List<MatchVoteUserEntity> voters = getVoters(matchVote);
        matchVote.setVoters(voters);
        matchVoteRepository.save(matchVote);
        return MatchVoteCreateResponse.of(
                matchVote.getMatchAt(),
                matchVote.getCreatedBy().getName()
        );
    }

    private List<MatchVoteUserEntity> getVoters(MatchVoteEntity matchVote) {
        List<UserEntity> voters = userRepository.findUserByMemberStatusAndRoleNot(
                MemberStatus.ENABLED,
                UserRole.MERCENARY
        );
        return voters.stream().map(
                voter -> MatchVoteUserEntity.create(voter, matchVote)
        ).collect(Collectors.toList());
    }
}
