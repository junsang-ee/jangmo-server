package com.jangmo.web.model.entity.user;

import com.jangmo.web.model.entity.common.SequentialEntity;
import com.jangmo.web.utils.EncryptUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;


@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Entity(name = "mercenary_code")
public class MercenaryCodeEntity extends SequentialEntity {

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "mercenary")
    private MercenaryEntity mercenary;

    private String code;

    public static MercenaryCodeEntity create(final MercenaryEntity mercenary, final String code) {
        return new MercenaryCodeEntity(
                mercenary,
                EncryptUtil.encode(code)
        );
    }



}
