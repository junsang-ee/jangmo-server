package com.jangmo.web.service.manager.board;

import com.jangmo.web.model.dto.request.board.manager.PostCreateRequest;
import com.jangmo.web.model.dto.request.board.manager.PostUpdateRequest;
import com.jangmo.web.model.dto.response.board.manager.PostCreateResponse;
import com.jangmo.web.model.entity.ReplyTargetEntity;
import com.jangmo.web.model.entity.board.BoardEntity;
import com.jangmo.web.model.entity.board.PostEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.repository.MemberRepository;
import com.jangmo.web.repository.board.BoardRepository;
import com.jangmo.web.repository.board.PostRepository;
import com.jangmo.web.repository.board.ReplyTargetRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class PostManagementServiceTest {
    final static String POST_CREATE = "게시판(Post) 생성 테스트";
    final static String POST_UPDATE = "게시판(Post) 수정 테스트";

    @Autowired PostManagementServiceImpl postManagementService;

    @Autowired BoardRepository boardRepository;

    @Autowired PostRepository postRepository;

    @Autowired MemberRepository memberRepository;

    @Autowired ReplyTargetRepository replyTargetRepository;

    @Value("${jangmo.admin.mobile}")
    String adminMobile;

    BoardEntity testParentBoard = null;

    MemberEntity admin = null;

    @BeforeEach
    void init(TestInfo testInfo) {
        initAdmin();
        String displayName = testInfo.getDisplayName();
        if (displayName.equals(POST_CREATE) || displayName.equals(POST_UPDATE)) {
            initBoard();
        }
    }



    @DisplayName(POST_CREATE)
    @Test
    @Transactional
    void postCreateTest() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(
                "testTitle",
                "testContent"
        );
        PostCreateResponse result = postManagementService.create(
                admin.getId(),
                testParentBoard.getId(),
                postCreateRequest
        );

        assertNotNull(result);
        assertEquals(result.getTitle(), postCreateRequest.getTitle());
        PostEntity post = postRepository.findByTitle(
                postCreateRequest.getTitle()
        ).orElseGet(() -> null);
        assertNotNull(post);
        assert post.getId() != null;
        ReplyTargetEntity replyTarget = replyTargetRepository.findById(
                post.getId()
        ).orElseGet(() -> null);
        assertNotNull(replyTarget);
        log.info("post title : {}", post.getTitle());
        log.info("post content : {}", post.getContent());
        assertEquals(post.getId(), replyTarget.getId());
        log.info("post id :: {}", post.getId());
        log.info("replyTarget id :: {}", replyTarget.getId());
    }


    @DisplayName(POST_UPDATE)
    @Test
    @Transactional
    void postUpdateTest() {
        PostCreateRequest createRequest = new PostCreateRequest(
                "testTile",
                "testContent"
        );
        PostEntity post = PostEntity.create(
                admin,
                createRequest,
                testParentBoard
        );
        postRepository.save(post);
        PostUpdateRequest updateRequest = new PostUpdateRequest(
                "updatedTestTile",
                "updatedTestContent"
        );
        postManagementService.update(
                post.getId(),
                updateRequest
        );
        PostEntity originPost = postRepository.findByTitle(
                createRequest.getTitle()
        ).orElseGet(() -> null);

        assertNull(originPost);

        log.info("post update title :: {}", post.getTitle());
        log.info("post update content :: {}", post.getContent());


    }

    @Transactional
    private void initBoard() {
        BoardEntity board = BoardEntity.create("test board");
        boardRepository.save(board);
        assertNotNull(board.getId());
        testParentBoard = board;
    }

    private void initAdmin() {
        admin = memberRepository.findByMobile(adminMobile).orElseGet(
                () -> null
        );
        assertNotNull(admin);
    }


}
