package com.jangmo.web.repository;

import com.jangmo.web.constants.match.MatchStatus;
import com.jangmo.web.model.entity.MatchEntity;
import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, String> {

    Optional<MatchEntity> findByMatchVote(MatchVoteEntity matchVote);

    List<MatchEntity> findAllByMatchAtAfterAndStatusIn(
            LocalDate now, List<MatchStatus> statuses
    );
}
