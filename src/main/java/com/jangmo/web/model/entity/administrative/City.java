package com.jangmo.web.model.entity.administrative;

import com.jangmo.web.model.entity.common.SequentialEntity;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@ToString
public class City extends SequentialEntity {

    private String name;

    @OneToMany(mappedBy = "city")
    private List<District> districts;
}
