package com.jangmo.web.model.entity.board;

import com.jangmo.web.constants.ReplyTargetType;
import com.jangmo.web.constants.board.ReplyActivationStatus;
import com.jangmo.web.model.dto.request.board.ReplyCreateRequest;
import com.jangmo.web.model.entity.CreationUserEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "reply")
public class ReplyEntity extends CreationUserEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReplyTargetType targetType;

    @Column(nullable = false)
    private String targetId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReplyActivationStatus status;

    private ReplyEntity(MemberEntity createdBy,
                        ReplyTargetType targetType,
                        String targetId,
                        String content) {
        super(createdBy);
        this.targetType = targetType;
        this.targetId = targetId;
        this.content = content;
        this.status = ReplyActivationStatus.ENABLED;
    }

    public static ReplyEntity create(final MemberEntity createdBy,
                                     final ReplyCreateRequest request) {
        return new ReplyEntity(
                createdBy,
                request.getTargetType(),
                request.getTargetId(),
                request.getContent()
        );
    }
}
