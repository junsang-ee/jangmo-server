package com.jangmo.web.service;

import com.jangmo.web.model.entity.MemberEntity;
import com.jangmo.web.model.entity.MercenaryEntity;

import java.util.Optional;

public interface UserService {
    Optional<MemberEntity> findMemberById(String memberId);

    Optional<MercenaryEntity> findMercenaryById(String mercenaryId);

    Optional<MercenaryEntity> findMercenaryByMobile(int mobile);

    Optional<MemberEntity> findMemberByMobile(int mobile);
}
