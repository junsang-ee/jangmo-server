package com.jangmo.web.repository.board;

import com.jangmo.web.model.entity.board.BoardEntity;
import com.jangmo.web.model.entity.board.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, String> {
    List<PostEntity> findByParentBoard(BoardEntity board);
}
