package com.jangmo.web.repository;

import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import com.jangmo.web.model.entity.vote.VoteEntity;
import com.jangmo.web.repository.query.VoteCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, String>,
        VoteCustomRepository {

    @Query(value = "SELECT m " +
                     "FROM match_vote m " +
                    "WHERE m.matchAt = :matchAt")
    List<MatchVoteEntity> findByMatchAt(@Param("matchAt") LocalDate matchAt);
}
