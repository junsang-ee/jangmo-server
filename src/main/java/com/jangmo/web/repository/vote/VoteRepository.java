package com.jangmo.web.repository.vote;

import com.jangmo.web.model.entity.vote.VoteEntity;
import com.jangmo.web.repository.query.VoteCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, String>,
        VoteCustomRepository {
}
