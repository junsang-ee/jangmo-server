package com.jangmo.web.service.manager.board;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.NotFoundException;
import com.jangmo.web.model.dto.request.board.manager.BoardCreateRequest;
import com.jangmo.web.model.dto.request.board.manager.BoardUpdateRequest;
import com.jangmo.web.model.dto.response.board.manager.BoardCreateResponse;
import com.jangmo.web.model.entity.board.BoardEntity;
import com.jangmo.web.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BoardManagementServiceImpl implements BoardManagementService {

    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public BoardCreateResponse create(BoardCreateRequest request) {
        BoardEntity board = BoardEntity.create(request.getName());
        boardRepository.save(board);
        return BoardCreateResponse.of(board.getName());
    }

    @Override
    @Transactional
    public void update(String boardId, BoardUpdateRequest request) {
        BoardEntity board = getBoardById(boardId);
        board.update(request.getName());
    }

    private BoardEntity getBoardById(String boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.BOARD_NOT_FOUND)
        );
    }

}
