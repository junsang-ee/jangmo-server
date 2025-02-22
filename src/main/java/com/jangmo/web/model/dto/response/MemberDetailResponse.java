package com.jangmo.web.model.dto.response;


import com.jangmo.web.constants.Gender;
import com.jangmo.web.constants.UserRole;
import com.jangmo.web.model.entity.user.MemberEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MemberDetailResponse {
    private final String name;
    private final String mobile;
    private final Gender gender;
    private final UserRole role;
    private final Instant createdAt;
    private final String cityName;
    private final String districtName;

    public static MemberDetailResponse of(final MemberEntity member) {
        return new MemberDetailResponse(
                member.getName(),
                member.getMobile(),
                member.getGender(),
                member.getRole(),
                member.getCreatedAt(),
                member.getCity().getName(),
                member.getDistrict().getName()
        );
    }
}
