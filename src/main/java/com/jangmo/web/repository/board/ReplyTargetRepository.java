package com.jangmo.web.repository.board;

import com.jangmo.web.model.entity.ReplyTargetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyTargetRepository extends JpaRepository<ReplyTargetEntity, String> {
}
