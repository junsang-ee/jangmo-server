package com.jangmo.web.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MobileCarrierType {
	KT("KT"),
	SKT("SKT"),
	LG("LG"),
	MVNO("알뜰폰");

	private final String displayName;

}
