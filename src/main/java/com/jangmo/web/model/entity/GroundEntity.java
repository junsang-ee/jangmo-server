package com.jangmo.web.model.entity;

import com.jangmo.web.model.entity.common.ModificationTimestampEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@ToString
@NoArgsConstructor
@Entity(name = "ground")
public class GroundEntity extends ModificationTimestampEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String streetAddress;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;
}
