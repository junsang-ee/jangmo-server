package com.jangmo.web.service;

import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.repository.MemberRepository;
import com.jangmo.web.repository.MercenaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final MemberRepository memberRepository;

    private final MercenaryRepository mercenaryRepository;

    @Override
    public Optional<MemberEntity> findMemberById(String memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public Optional<MercenaryEntity> findMercenaryById(String mercenaryId) {
        return mercenaryRepository.findById(mercenaryId);
    }

    @Override
    public Optional<MercenaryEntity> findMercenaryByMobile(String mobile) {
        return mercenaryRepository.findByMobile(mobile);
    }

    @Override
    public Optional<MemberEntity> findMemberByMobile(String mobile) {
        return memberRepository.findByMobile(mobile);
    }
}
