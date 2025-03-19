package com.jangmo.web.service.manager;


import com.jangmo.web.model.dto.response.UserListResponse;
import com.jangmo.web.model.entity.user.UserEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserManagementService {
    void approveMercenary(String mercenaryId, String matchId);

    void approveMember(String memberId);

    List<UserEntity> getApprovalUsers();

    Page<UserListResponse> getUsers(String myId, Pageable pageable);
}
