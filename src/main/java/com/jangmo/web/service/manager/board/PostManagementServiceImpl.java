package com.jangmo.web.service.manager.board;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.NotFoundException;
import com.jangmo.web.model.dto.request.board.manager.PostCreateRequest;
import com.jangmo.web.model.dto.request.board.manager.PostUpdateRequest;
import com.jangmo.web.model.dto.response.board.manager.PostCreateResponse;
import com.jangmo.web.model.entity.board.BoardEntity;
import com.jangmo.web.model.entity.board.PostEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.repository.user.MemberRepository;
import com.jangmo.web.repository.board.BoardRepository;
import com.jangmo.web.repository.board.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostManagementServiceImpl implements PostManagementService {

	private final BoardRepository boardRepository;

	private final PostRepository postRepository;

	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public PostCreateResponse create(String memberId, String boardId, PostCreateRequest request) {
		BoardEntity parentBoard = getBoardById(boardId);
		MemberEntity createdBy = getMemberById(memberId);
		PostEntity post = PostEntity.create(createdBy, request, parentBoard);
		postRepository.save(post);
		return PostCreateResponse.of(post.getTitle());
	}

	@Override
	@Transactional
	public void update(String postId, PostUpdateRequest request) {
		PostEntity post = getPostById(postId);
		post.update(request);
	}


	private BoardEntity getBoardById(String boardId) {
		return boardRepository.findById(boardId).orElseThrow(
			() -> new NotFoundException(ErrorMessage.BOARD_NOT_FOUND)
		);
	}

	private PostEntity getPostById(String postId) {
		return postRepository.findById(postId).orElseThrow(
			() -> new NotFoundException(ErrorMessage.POST_NOT_FOUND)
		);
	}

	private MemberEntity getMemberById(String memberId) {
		return memberRepository.findById(memberId).orElseThrow(
			() -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND)
		);
	}

}
