package com.jangmo.web.service;

import com.jangmo.web.model.entity.MemberEntity;

import java.util.Optional;

public interface UserService {
    Optional<MemberEntity> get(String userId);

    Optional<MemberEntity> getByMobile(int mobile);
}
