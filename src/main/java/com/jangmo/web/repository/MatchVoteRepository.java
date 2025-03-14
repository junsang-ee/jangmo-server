package com.jangmo.web.repository;

import com.jangmo.web.model.entity.vote.MatchVoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MatchVoteRepository extends JpaRepository<MatchVoteEntity, String> {

    List<MatchVoteEntity> findByMatchAt(LocalDate matchAt);
}
