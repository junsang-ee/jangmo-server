package com.jangmo.web.repository;

import com.jangmo.web.model.entity.vote.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, String> {
}
