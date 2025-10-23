package com.jangmo.web.model.entity.board;

import com.jangmo.web.constants.board.PostActivationStatus;
import com.jangmo.web.model.dto.request.board.manager.PostCreateRequest;
import com.jangmo.web.model.dto.request.board.manager.PostUpdateRequest;
import com.jangmo.web.model.entity.ReplyTargetEntity;
import com.jangmo.web.model.entity.user.MemberEntity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;


import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "post")
public class PostEntity extends ReplyTargetEntity {

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentBoard", nullable = false)
    private BoardEntity parentBoard;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostActivationStatus status;


    private PostEntity(MemberEntity createdBy,
                       String title,
                       String content,
                       BoardEntity board) {
        super(createdBy);
        this.title = title;
        this.content = content;
        this.parentBoard = board;
        this.status = PostActivationStatus.ENABLED;
    }

    public static PostEntity create(final MemberEntity createdBy,
                                    final PostCreateRequest createRequest,
                                    final BoardEntity board) {
        return new PostEntity(
                createdBy,
                createRequest.getTitle(),
                createRequest.getContent(),
                board
        );
    }

    public void update(PostUpdateRequest update) {
        this.title = update.getTitle();
        this.content = update.getContent();
    }

}
