package com.jangmo.web.service.board;

import com.jangmo.web.model.dto.request.board.manager.BoardCreateRequest;
import com.jangmo.web.model.dto.response.board.BoardListResponse;
import com.jangmo.web.model.entity.board.BoardEntity;
import com.jangmo.web.repository.board.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@Slf4j
@SpringBootTest
public class BoardServiceTest {

    @Autowired BoardServiceImpl boardService;

    @Autowired BoardRepository boardRepository;

    @DisplayName("Board(게시판) 리스트 가져오기 테스트")
    @Test
    void getBoardListTest() {
        BoardCreateRequest firstCRequest = new BoardCreateRequest(
                "first test board"
        );

        BoardCreateRequest secondRequest = new BoardCreateRequest(
                "second test board"
        );

        BoardCreateRequest thirdRequest = new BoardCreateRequest(
                "third test board"
        );

        BoardEntity firstBoard = BoardEntity.create(firstCRequest.getName());
        BoardEntity secondBoard = BoardEntity.create(secondRequest.getName());
        BoardEntity thirdBoard = BoardEntity.create(thirdRequest.getName());

        List<BoardEntity> boards = Arrays.asList(
                firstBoard, secondBoard, thirdBoard
        );

        boardRepository.saveAll(boards);
        int boardCount = boardRepository.findAll().size();
        assertEquals(boardCount, boards.size());
        List<BoardListResponse> boardList = boardService.list();
        assertEquals(boardCount, boardList.size());
        log.info("boardList size : {}", boardList.size());
        boardList.forEach(
                board -> log.info("board name : {}", board.getName())
        );
    }
}
