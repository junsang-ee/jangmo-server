package com.jangmo.web.model.entity.administrative;

import com.jangmo.web.model.SequentialEntity;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@ToString
public class District extends SequentialEntity {
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city", nullable = false)
    private City city;
}
