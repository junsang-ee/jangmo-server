package com.jangmo.web.service.manager;


import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.user.MercenaryStatus;
import com.jangmo.web.model.dto.request.UserListSearchRequest;
import com.jangmo.web.model.dto.response.MemberDetailResponse;
import com.jangmo.web.model.dto.response.MercenaryDetailResponse;
import com.jangmo.web.model.dto.response.UserListResponse;
import com.jangmo.web.model.entity.user.UserEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserManagementService {
	void approveMercenary(String mercenaryId, String matchId);

	void approveMember(String memberId);

	List<UserEntity> getApprovalUsers();

	Page<UserListResponse> getUserList(String myId, UserListSearchRequest request, Pageable pageable);

	MemberDetailResponse getMemberDetail(String memberId);

	MercenaryDetailResponse getMercenaryDetail(String mercenaryId);

	void updateMemberStatus(String memberId, MemberStatus status);

	void updateMercenaryStatus(String mercenaryId, MercenaryStatus status);

	void updateMemberRole(String memberId, UserRole role);
}
