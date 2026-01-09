package com.jangmo.web.constants.vote;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum VoteModeType {
	SINGLE, MULTIPLE;

	@JsonCreator
	public static VoteModeType getValue(String value) {
		for(VoteModeType type : values()) {
			if(type.name().equals(value)) {
				return type;
			}
		}
		return null;
	}
}
