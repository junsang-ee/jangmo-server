package com.jangmo.web.service.board;

import com.jangmo.web.model.dto.response.board.BoardListResponse;
import com.jangmo.web.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public List<BoardListResponse> list() {
        return boardRepository.findAll().stream().map(
                BoardListResponse::of
        ).collect(Collectors.toList());
    }
}
