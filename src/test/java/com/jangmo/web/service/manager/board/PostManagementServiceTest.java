package com.jangmo.web.service.manager.board;

import com.jangmo.web.model.dto.request.board.manager.PostCreateRequest;
import com.jangmo.web.model.dto.response.board.manager.PostCreateResponse;
import com.jangmo.web.model.entity.board.BoardEntity;
import com.jangmo.web.model.entity.board.PostEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.repository.MemberRepository;
import com.jangmo.web.repository.board.BoardRepository;
import com.jangmo.web.repository.board.PostRepository;
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
public class PostManagementServiceTest {

    @Autowired PostManagementServiceImpl postManagementService;

    @Autowired BoardRepository boardRepository;

    @Autowired PostRepository postRepository;

    @Autowired MemberRepository memberRepository;

    @DisplayName("Post(게시판) 생성 테스트")
    @Test
    @Transactional
    void postCreateTest() {
        BoardEntity board = BoardEntity.create("테스트 게시판");
        boardRepository.save(board);
        assertNotNull(board);
        log.info("board id :: {}", board.getId());
        MemberEntity admin = memberRepository.findByMobile("01043053451").get();
        PostCreateRequest postCreateRequest = new PostCreateRequest(
                "게시판 테스트 title",
                "게시판 테스트 content"
        );
        PostCreateResponse result = postManagementService.create(
                admin.getId(),
                board.getId(),
                postCreateRequest
        );

        assertNotNull(result);
        assertEquals(result.getTitle(), postCreateRequest.getTitle());
        List<PostEntity> posts = postRepository.findAll();
        PostEntity post = posts.get(0);
        assertNotNull(post);
        log.info("post title : {}", post.getTitle());
        log.info("post content : {}", post.getContent());
    }
}
