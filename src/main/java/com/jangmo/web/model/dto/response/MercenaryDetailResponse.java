package com.jangmo.web.model.dto.response;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.user.MercenaryStatus;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MercenaryDetailResponse {
	private final String id;
	private final String name;
	private final String mobile;
	private final Gender gender;
	private final UserRole role;
	private final MercenaryStatus status;
	private final Instant createdAt;


	public static MercenaryDetailResponse of(final MercenaryEntity mercenary) {
		return new MercenaryDetailResponse(
			mercenary.getId(),
			mercenary.getName(),
			mercenary.getMobile(),
			mercenary.getGender(),
			mercenary.getRole(),
			mercenary.getStatus(),
			mercenary.getCreatedAt()
		);
	}

}
