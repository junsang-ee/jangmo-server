package com.jangmo.web.service;

import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.MemberStatus;
import com.jangmo.web.model.dto.request.MatchVoteCreateRequest;
import com.jangmo.web.model.entity.MatchVoteEntity;
import com.jangmo.web.model.entity.MatchVoteUserEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.repository.MatchVoteRepository;
import com.jangmo.web.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VoteServiceImpl implements VoteService {

    private final MatchVoteRepository matchVoteRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public MatchVoteEntity createMatchVote(UserEntity writer, MatchVoteCreateRequest request) {
        MatchVoteEntity matchVote = MatchVoteEntity.create(writer, request);
        addVoters(matchVote);
        return matchVoteRepository.save(matchVote);
    }

    private void addVoters(MatchVoteEntity matchVote) {
        List<UserEntity> users = userRepository.findUserByMemberStatusAndRoleNot(
                MemberStatus.ENABLED,
                UserRole.MERCENARY
        );
        for (UserEntity user : users) {
            MatchVoteUserEntity voter = MatchVoteUserEntity.create(user, matchVote);
            matchVote.addVoter(voter);
        }
    }
}
