package com.jangmo.web.service.manager.board;

import com.jangmo.web.model.dto.request.board.manager.BoardCreateRequest;
import com.jangmo.web.model.dto.request.board.manager.BoardUpdateRequest;
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

    static final String BOARD_CREATE = "게시판(Board) 생성 테스트";
    static final String BOARD_UPDATE = "게시판(Board) 수정 테스트";


    @DisplayName(BOARD_CREATE)
    @Test
    @Transactional
    void boardCreateTest() {
        BoardCreateRequest createRequest = new BoardCreateRequest("게시판 테스트");
        BoardCreateResponse response = boardManagementService.create(createRequest);
        assertNotNull(response);
        BoardEntity board = boardRepository.findByName(createRequest.getName()).orElseGet(
                () -> null
        );
        assertNotNull(board);
        assertEquals(response.getName(), board.getName());
        log.info("board name :: {}", board.getName());
    }

    @DisplayName(BOARD_UPDATE)
    @Test
    @Transactional
    void boardUpdateTest() {
        BoardCreateRequest createRequest = new BoardCreateRequest("게시판 테스트");
        BoardEntity board = BoardEntity.create(createRequest.getName());
        boardRepository.save(board);
        assertNotNull(board.getId());
        assertNotNull(
                boardRepository.findByName(board.getName()).orElseGet(
                        () -> null
                )
        );
        log.info("board name : {}", board.getName());
        assertEquals(createRequest.getName(), board.getName());
        BoardUpdateRequest updateRequest = new BoardUpdateRequest("게시판 수정 테스트");
        boardManagementService.update(board.getId(), updateRequest);

        assertEquals(board.getName(), updateRequest.getName());

        assertNotNull(
                boardRepository.findByName(updateRequest.getName()).orElseGet(
                        () -> null
                )
        );
        log.info("update board name :: {}", board.getName());
    }

}
