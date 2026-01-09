package com.jangmo.web.model.dto.response;

import com.jangmo.web.constants.UserRole;

import com.jangmo.web.model.vo.UserStatusVO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PUBLIC;

@Getter
@RequiredArgsConstructor(access = PUBLIC)
public class UserListResponse {
	private final String userId;
	private final String userName;
	private final UserRole role;
	private final UserStatusVO status;
}
