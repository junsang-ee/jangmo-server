package com.jangmo.web.repository.query;

import com.jangmo.web.model.dto.request.UserListSearchRequest;
import com.jangmo.web.model.dto.response.UserListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCustomRepository {
	Page<UserListResponse> findByStatusAndUserRole(
		String adminId,
		String myId,
		UserListSearchRequest request,
		Pageable pageable
	);
}
