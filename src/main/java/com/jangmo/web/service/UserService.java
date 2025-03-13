package com.jangmo.web.service;

import com.jangmo.web.model.dto.request.MemberUpdatePasswordRequest;
import com.jangmo.web.model.dto.response.MemberDetailResponse;
import com.jangmo.web.model.dto.response.MercenaryDetailResponse;
import com.jangmo.web.model.dto.response.UserDetailResponse;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.model.entity.user.MercenaryEntity;
import com.jangmo.web.model.entity.user.UserEntity;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserService {
    Optional<MemberEntity> findMemberById(String memberId);

    Optional<MercenaryEntity> findMercenaryById(String mercenaryId);

    Optional<MercenaryEntity> findMercenaryByMobile(String mobile);

    Optional<MemberEntity> findMemberByMobile(String mobile);

    Optional<UserEntity> findByMobile(String mobile);

    Optional<UserEntity> findById(String id);

    UserDetailResponse getDetail(String userId);

    MemberDetailResponse getMemberDetail(String memberId);

    MercenaryDetailResponse getMercenaryDetail(String mercenaryId);

    void updatePassword(String memberId, String oldPassword, String newPassword);

    void updateAddress(String memberId, Long cityId, Long districtId);




}
