package com.jangmo.web.service.manager;


import com.jangmo.web.model.entity.user.UserEntity;

import java.util.List;

public interface UserManagementService {
    void approveMercenary(String mercenaryId, String matchId);

    void approveMember(String memberId);

    List<UserEntity> getApprovalUsers();
}
