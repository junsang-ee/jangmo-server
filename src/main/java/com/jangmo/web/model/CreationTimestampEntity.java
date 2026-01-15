package com.jangmo.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Column;
import java.time.Instant;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CreationTimestampEntity extends BaseUuidEntity {

	@CreatedDate
	@Column(nullable = false, updatable = false)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@JsonFormat(shape = JsonFormat.Shape.STRING,
		pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
		timezone = "Asia/Seoul")
	private Instant createdAt;
}
