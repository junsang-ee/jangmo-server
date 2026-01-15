package com.jangmo.web.model.entity;

import com.jangmo.web.model.entity.user.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;

import static lombok.AccessLevel.PROTECTED;
import static jakarta.persistence.InheritanceType.JOINED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Inheritance(strategy = JOINED)
@Entity(name = "reply_target")
public abstract class ReplyTargetEntity extends CreationUserEntity {

	protected ReplyTargetEntity(MemberEntity createdBy) {
		super(createdBy);
	}

}
