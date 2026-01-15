package com.jangmo.web.repository.board;

import com.jangmo.web.model.entity.board.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, String> {
}
