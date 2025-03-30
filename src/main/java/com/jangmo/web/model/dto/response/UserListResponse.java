package com.jangmo.web.model.dto.response;

import com.jangmo.web.constants.UserRole;
import com.jangmo.web.model.entity.user.UserEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class UserListResponse {
    private final String userId;
    private final String userName;
    private final UserRole role;

    public static UserListResponse of(final UserEntity user) {
        return new UserListResponse(
                user.getId(),
                user.getName(),
                user.getRole()
        );
    }

}
