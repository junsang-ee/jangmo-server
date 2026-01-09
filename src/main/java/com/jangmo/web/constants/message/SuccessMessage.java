package com.jangmo.web.constants.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessMessage {
	RESULT("0"),
	EMPTY("1");

	private final String code;

	public int getCode() {
        return Integer.parseInt(code);
    }

	public String resName() {
        return "response.success." + name().toLowerCase().replaceAll("_", ".") + ".message";
    }

	public static SuccessMessage from(String code) {
		for (SuccessMessage value : values()) {
			if (value.code.equals(code)) {
				return value;
			}
		}
		throw new IllegalArgumentException(code);
	}

}
