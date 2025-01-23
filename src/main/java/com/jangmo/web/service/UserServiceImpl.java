package com.jangmo.web.service;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.custom.NotFoundException;
import com.jangmo.web.model.dto.response.UserDetailResponse;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.repository.MemberRepository;
import com.jangmo.web.repository.MercenaryRepository;
import com.jangmo.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final MemberRepository memberRepository;

    private final MercenaryRepository mercenaryRepository;

    private final UserRepository userRepository;

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

    @Override
    public Optional<UserEntity> findByMobile(String mobile) {
        return userRepository.findByMobile(mobile);
    }

    @Override
    public Optional<UserEntity> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public UserDetailResponse getDetail(String userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.USER_NOT_FOUND)
        );
        return UserDetailResponse.of(user);
    }
}
