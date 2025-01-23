package com.jangmo.web.model.dto.response;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.model.entity.user.UserEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class UserDetailResponse {
    private final String name;

    private final String mobile;

    private final Gender gender;

    public static UserDetailResponse of(final UserEntity user) {
        return new UserDetailResponse(
                user.getName(),
                user.getMobile(),
                user.getGender()
        );
    }

}
