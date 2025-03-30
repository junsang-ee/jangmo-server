package com.jangmo.web.model.dto.response;


import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.model.entity.user.MemberEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MemberDetailResponse {
    private final String name;
    private final String mobile;
    private final LocalDate birth;
    private final Gender gender;
    private final UserRole role;
    private final MemberStatus status;
    private final Instant createdAt;
    private final Long cityId;
    private final String cityName;
    private final Long districtId;
    private final String districtName;

    public static MemberDetailResponse of(final MemberEntity member) {
        return new MemberDetailResponse(
                member.getName(),
                member.getMobile(),
                member.getBirth(),
                member.getGender(),
                member.getRole(),
                member.getStatus(),
                member.getCreatedAt(),
                member.getCity().getId(),
                member.getCity().getName(),
                member.getDistrict().getId(),
                member.getDistrict().getName()
        );
    }
}
