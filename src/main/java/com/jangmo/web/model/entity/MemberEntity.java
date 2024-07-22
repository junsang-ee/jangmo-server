package com.jangmo.web.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "member")
public class MemberEntity extends AbstractUserEntity {

    @Column(nullable = false)
    private int birth;

    @Column(nullable = false)
    private String password;

}
