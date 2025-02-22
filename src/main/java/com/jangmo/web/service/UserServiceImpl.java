package com.jangmo.web.service;

import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.custom.AuthException;
import com.jangmo.web.exception.custom.InvalidStateException;
import com.jangmo.web.exception.custom.NotFoundException;
import com.jangmo.web.model.dto.response.MemberDetailResponse;
import com.jangmo.web.model.dto.response.MercenaryDetailResponse;
import com.jangmo.web.model.dto.response.UserDetailResponse;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.model.entity.user.UserEntity;
import com.jangmo.web.repository.MemberRepository;
import com.jangmo.web.repository.MercenaryRepository;
import com.jangmo.web.repository.UserRepository;
import com.jangmo.web.utils.EncryptUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

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

    @Override
    public MemberDetailResponse getMemberDetail(String memberId) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND)
        );
        return MemberDetailResponse.of(member);
    }

    @Override
    public MercenaryDetailResponse getMercenaryDetail(String mercenaryId) {
        MercenaryEntity mercenary = mercenaryRepository.findById(mercenaryId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MERCENARY_NOT_FOUND)
        );
        return MercenaryDetailResponse.of(mercenary);
    }

    @Override
    @Transactional
    public void updatePassword(String memberId, String oldPassword, String newPassword) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND)
        );
        if (oldPassword != null) {
            checkMemberAccount(member, oldPassword);
        }
        member.updatePassword(newPassword);
    }

    private void checkMemberAccount(MemberEntity member, String oldPassword) {
        if (EncryptUtil.matches(oldPassword, member.getPassword())) {
            switch (member.getStatus()) {
                case DISABLED:
                    throw new AuthException(ErrorMessage.AUTH_DISABLED);
                case RETIRED:
                    throw new AuthException(ErrorMessage.AUTH_RETIRED);
                case PENDING:
                    throw new AuthException(ErrorMessage.AUTH_UNAUTHENTICATED);
                default: return;
            }
        }
        throw new InvalidStateException(ErrorMessage.AUTH_PASSWORD_INVALID);
    }

    private UserEntity getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.USER_NOT_FOUND)
        );
    }
}
