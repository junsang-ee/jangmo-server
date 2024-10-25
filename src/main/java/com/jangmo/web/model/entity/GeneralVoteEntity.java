package com.jangmo.web.model.entity;

import com.jangmo.web.model.entity.common.CreationTimestampEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "general_vote")
public class GeneralVoteEntity extends CreationTimestampEntity {

}
