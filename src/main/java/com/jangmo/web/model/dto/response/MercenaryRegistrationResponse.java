package com.jangmo.web.model.dto.response;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MercenaryRegistrationResponse {
	private final String name;
	private final String mobile;
	private final Gender gender;

	public static MercenaryRegistrationResponse of(final MercenaryEntity mercenary) {
		return new MercenaryRegistrationResponse(
			mercenary.getName(),
			mercenary.getMobile(),
			mercenary.getGender()
		);
	}
}
