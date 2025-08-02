package com.jangmo.web.service.board;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.NotFoundException;
import com.jangmo.web.model.dto.response.board.PostListResponse;
import com.jangmo.web.model.entity.board.BoardEntity;
import com.jangmo.web.model.entity.board.PostEntity;
import com.jangmo.web.repository.board.BoardRepository;
import com.jangmo.web.repository.board.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final BoardRepository boardRepository;

    private final PostRepository postRepository;
    @Override
    public List<PostListResponse> list(String parentBoardId) {
        BoardEntity parentBoard = getBoardById(parentBoardId);
        return postRepository.findByParentBoard(parentBoard).stream().map(
                PostListResponse::of
        ).collect(Collectors.toList());
    }

    @Override
    public PostEntity detail(String postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.POST_NOT_FOUND)
        );
    }

    private BoardEntity getBoardById(String boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.BOARD_NOT_FOUND)
        );
    }
}
