package com.jangmo.web.model.entity.user;

import com.jangmo.web.model.SequentialEntity;
import com.jangmo.web.model.entity.MatchEntity;

import com.jangmo.web.utils.EncryptUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.ManyToOne;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Entity(name = "mercenary_transient")
public class MercenaryTransientEntity extends SequentialEntity {

    @Column(nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match", nullable = false)
    private MatchEntity match;

    public static MercenaryTransientEntity create(final String code,
                                                  final MatchEntity match) {
        return new MercenaryTransientEntity(
                EncryptUtil.encode(code),
                match
        );
    }

    public void updateCode(String code) {
        this.code = EncryptUtil.encode(code);
    }
}
