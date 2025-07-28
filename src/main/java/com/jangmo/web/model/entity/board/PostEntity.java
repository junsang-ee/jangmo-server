package com.jangmo.web.model.entity.board;


import com.jangmo.web.constants.PostActivationStatus;
import com.jangmo.web.model.entity.user.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "post")
public class PostEntity extends AbstractPostEntity {

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private PostActivationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentBoard", nullable = false)
    private BoardEntity parentBoard;

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
                                    final String title,
                                    final String content,
                                    final BoardEntity board) {
        return new PostEntity(createdBy, title, content, board);
    }




}
