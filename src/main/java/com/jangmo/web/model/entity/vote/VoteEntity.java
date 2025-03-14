package com.jangmo.web.model.entity.vote;

import com.jangmo.web.constants.VoteType;
import com.jangmo.web.model.entity.CreationUserEntity;

import com.jangmo.web.model.entity.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Inheritance;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

import java.time.LocalDate;

import static javax.persistence.InheritanceType.JOINED;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Inheritance(strategy = JOINED)
@Entity(name = "vote")
public class VoteEntity extends CreationUserEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "end_at", nullable = false)
    private LocalDate endAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private VoteType type;

    protected VoteEntity(UserEntity user, String title, LocalDate endAt, VoteType type) {
        super(user);
        this.title = title;
        this.endAt = endAt;
        this.type = type;
    }

}
