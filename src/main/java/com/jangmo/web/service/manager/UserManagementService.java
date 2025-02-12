package com.jangmo.web.service.manager;

import com.jangmo.web.model.entity.user.MercenaryEntity;

public interface UserManagementService {
    void approveMercenary(String mercenaryId, String matchId);

    void approveMember(String memberId);
}
