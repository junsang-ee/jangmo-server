package com.jangmo.web.repository.vote.user;

import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import com.jangmo.web.model.entity.vote.user.MatchVoteUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchVoteUserRepository extends JpaRepository<MatchVoteUserEntity, String> {

    Optional<MatchVoteUserEntity> findByMatchVoteAndVoterId(MatchVoteEntity matchVote,  String voterId);
}
