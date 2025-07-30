package com.jangmo.web.service.manager.board;

import com.jangmo.web.model.dto.request.board.manager.BoardCreateRequest;
import com.jangmo.web.model.dto.response.board.manager.BoardCreateResponse;
import com.jangmo.web.model.entity.board.BoardEntity;
import com.jangmo.web.repository.board.BoardRepository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
public class BoardManagementServiceTest {

    @Autowired BoardManagementServiceImpl boardManagementService;

    @Autowired BoardRepository boardRepository;

    @DisplayName("board(게시판) 생성 테스트")
    @Test
    @Transactional
    void boardCreateTest() {
        BoardCreateRequest createRequest = new BoardCreateRequest("게시판 테스트");

        BoardCreateResponse response = boardManagementService.create(createRequest);
        assertNotNull(response);
        List<BoardEntity> boards = boardRepository.findAll();
        assertEquals(1, boards.size());
        log.info("boards size :: {}", boards.size());
        BoardEntity board = boards.get(0);
        assertNotNull(response.getName(), board.getName());
        log.info("response name :: {}", response.getName());
        log.info("board name :: {}", board.getName());
    }

}
