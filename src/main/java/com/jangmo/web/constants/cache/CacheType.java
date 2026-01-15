package com.jangmo.web.constants.cache;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Getter
@NoArgsConstructor
@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum CacheType {
	@FieldNameConstants.Include SIGNUP_CODE("SIGNUP_CODE", 3),
	@FieldNameConstants.Include RESET_CODE("RESET_CODE", 3),
	@FieldNameConstants.Include SIGNUP_VERIFIED("SIGNUP_VERIFIED", 10),
	@FieldNameConstants.Include RESET_VERIFIED("RESET_VERIFIED", 10);

	private String name;

	private int expiredTime;

	private int maximumSize;

	CacheType(String name, int expiredTime) {
		this.name = name;
		this.expiredTime = expiredTime;
		this.maximumSize = 10000;
	}
}
