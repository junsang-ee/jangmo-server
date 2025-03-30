package com.jangmo.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
public abstract class ModificationTimestampEntity extends CreationTimestampEntity{
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(nullable = false)
    @LastModifiedDate
    private Instant updatedAt;
}
