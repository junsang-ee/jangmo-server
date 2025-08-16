package com.jangmo.web.model.entity.vote;

import com.jangmo.web.constants.VoteType;
import com.jangmo.web.constants.vote.VoteSelectionType;
import com.jangmo.web.model.entity.CommentTargetEntity;
import com.jangmo.web.model.entity.CreationUserEntity;

import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;

import static javax.persistence.InheritanceType.JOINED;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Inheritance(strategy = JOINED)
@Entity(name = "vote")
public class VoteEntity extends CommentTargetEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate startAt;

    @Column(nullable = false)
    private LocalDate endAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteSelectionType selectionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteType voteType;

    protected VoteEntity(MemberEntity createdBy,
                         String title,
                         LocalDate startAt,
                         LocalDate endAt,
                         VoteSelectionType selectionType,
                         VoteType voteType) {
        super(createdBy);
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.selectionType = selectionType;
        this.voteType = voteType;
    }

}
