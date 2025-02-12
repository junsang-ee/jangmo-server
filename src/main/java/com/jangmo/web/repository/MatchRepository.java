package com.jangmo.web.repository;

import com.jangmo.web.model.entity.MatchEntity;
import com.jangmo.web.model.entity.MatchVoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, String> {

    Optional<MatchEntity> findByMatchVote(MatchVoteEntity matchVote);
}
