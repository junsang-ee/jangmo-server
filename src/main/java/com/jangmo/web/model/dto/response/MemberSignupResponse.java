package com.jangmo.web.model.dto.response;

import com.jangmo.web.constants.Gender;
import com.jangmo.web.model.entity.user.MemberEntity;
import com.jangmo.web.utils.AgeUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberSignupResponse {
    private final String name;
    private final String mobile;
    private final int old;
    private final String address;
    private final Gender gender;

    public static MemberSignupResponse of(final MemberEntity member) {
        String address = member.getCity().getName() +
                " " + member.getDistrict().getName();
        return new MemberSignupResponse(
                member.getName(),
                member.getMobile(),
                AgeUtil.calculate(member.getBirth()),
                address,
                member.getGender()
        );
    }
}
