package com.jangmo.web.model.entity.board;

import com.jangmo.web.constants.PostActivationStatus;
import com.jangmo.web.model.ModificationTimestampEntity;
import com.jangmo.web.model.entity.user.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@MappedSuperclass
public abstract class AbstractPostEntity extends ModificationTimestampEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by",
            nullable = false,
            updatable = false)
    private MemberEntity createdBy;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostActivationStatus status;

    protected AbstractPostEntity(MemberEntity createdBy) {
        this.createdBy = createdBy;
        this.status = PostActivationStatus.ENABLED;
    }

}
