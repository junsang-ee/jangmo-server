package com.jangmo.web.model.entity.administrative;

import com.jangmo.web.model.entity.common.SequentialEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class City extends SequentialEntity {

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "city")
    private List<District> districts;

    public static City of(final String name) {
        return new City(name);
    }

    protected City(String name) {
        this.name = name;
    }
}
