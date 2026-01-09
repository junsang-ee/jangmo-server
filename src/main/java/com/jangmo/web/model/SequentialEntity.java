package com.jangmo.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import org.springframework.data.domain.Persistable;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.GenerationType;

@MappedSuperclass
public abstract class SequentialEntity implements Persistable<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private @Getter Long id;

	@Override
	@JsonIgnore
	public boolean isNew() {
			return id == null;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).omitNullValues()
			.add("id", id).toString();
	}
}
