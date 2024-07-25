package com.jangmo.web.service;

import com.jangmo.web.model.dto.request.MemberSignupRequest;
import com.jangmo.web.model.entity.MemberEntity;
import com.jangmo.web.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MemberEntity signUp(MemberSignupRequest signup) {
        MemberEntity member = MemberEntity.create(signup);
        return memberRepository.save(member);

    }
}
