package com.jangmo.web.service;

import com.jangmo.web.model.dto.request.MemberSignupRequest;
import com.jangmo.web.model.entity.MemberEntity;

public interface AuthService {
    MemberEntity signUp(MemberSignupRequest signup);
}
