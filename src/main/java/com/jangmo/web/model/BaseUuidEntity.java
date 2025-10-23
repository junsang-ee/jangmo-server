package com.jangmo.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.domain.Persistable;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@MappedSuperclass
public abstract class BaseUuidEntity implements Persistable<String> {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @JsonIgnore
    @Override
    public boolean isNew() {
        return id == null;
    }
}
