package com.jangmo.web.repository.board;

import com.jangmo.web.model.entity.board.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, String> {
    Optional<BoardEntity> findByName(String name);
}
