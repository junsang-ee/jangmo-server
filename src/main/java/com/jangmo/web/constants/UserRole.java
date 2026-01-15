package com.jangmo.web.constants;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserRole {
	ADMIN,
	MANAGER,
	MEMBER,
	MERCENARY;

	@JsonCreator
	public static UserRole getValue(String value) {
		for(UserRole role : values()) {
			if(role.name().equals(value)) {
				return role;
			}
		}
		return null;
	}

}
