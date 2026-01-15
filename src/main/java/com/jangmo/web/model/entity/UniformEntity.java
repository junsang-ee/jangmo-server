package com.jangmo.web.model.entity;

import com.jangmo.web.model.ModificationTimestampEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity(name = "uniform")
@NoArgsConstructor(access = PROTECTED)
public class UniformEntity extends ModificationTimestampEntity {

	private int backNumber;

	private UniformEntity(int backNumber) {
		this.backNumber = backNumber;
	}

	public static UniformEntity create(final int backNumber) {
		return new UniformEntity(backNumber);
	}

	public void updateBackNumber(int backNumber) {
		this.backNumber = backNumber;
	}
}
