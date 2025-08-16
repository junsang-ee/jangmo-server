package com.jangmo.web.model.entity;

import com.jangmo.web.model.entity.user.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

import static javax.persistence.InheritanceType.JOINED;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Inheritance(strategy = JOINED)
@Entity(name = "comment_target")
public abstract class CommentTargetEntity extends CreationUserEntity {

    protected CommentTargetEntity(MemberEntity createdBy) {
        super(createdBy);
    }

}
