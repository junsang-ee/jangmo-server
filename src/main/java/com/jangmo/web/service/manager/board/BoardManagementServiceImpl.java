package com.jangmo.web.service.manager.board;

import com.jangmo.web.model.dto.request.board.BoardCreateRequest;
import com.jangmo.web.model.dto.response.board.BoardCreateResponse;
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

}
