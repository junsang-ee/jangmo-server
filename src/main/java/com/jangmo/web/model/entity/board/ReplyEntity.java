package com.jangmo.web.model.entity.board;

import com.jangmo.web.constants.board.ReplyActivationStatus;
import com.jangmo.web.model.dto.request.board.ReplyCreateRequest;
import com.jangmo.web.model.entity.ReplyTargetEntity;
import com.jangmo.web.model.entity.CreationUserEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "reply")
public class ReplyEntity extends CreationUserEntity {

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_target", nullable = false)
    private ReplyTargetEntity target;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReplyActivationStatus status;

    private ReplyEntity(MemberEntity createdBy,
                        ReplyTargetEntity target,
                        String content) {
        super(createdBy);
        this.content = content;
        this.target = target;
        this.status = ReplyActivationStatus.ENABLED;
    }

    public static ReplyEntity create(final MemberEntity createdBy,
                                     final ReplyTargetEntity target,
                                     final ReplyCreateRequest request) {
        return new ReplyEntity(
                createdBy, target, request.getContent()
        );
    }
}
