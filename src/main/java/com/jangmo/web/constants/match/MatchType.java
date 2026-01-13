package com.jangmo.web.constants.match;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MatchType {
	FUTSAL, FOOTBALL;

	@JsonCreator
	public static MatchType getType(String value) {
		for(MatchType type : values()) {
			if(type.name().equals(value)) {
				return type;
			}
		}
		return null;
	}
}
