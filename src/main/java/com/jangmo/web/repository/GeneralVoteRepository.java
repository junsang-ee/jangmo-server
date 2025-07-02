package com.jangmo.web.repository;

import com.jangmo.web.model.entity.vote.GeneralVoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralVoteRepository extends JpaRepository<GeneralVoteEntity, String> {
}
