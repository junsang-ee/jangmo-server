package com.jangmo.web.model.dto.request;

import com.jangmo.web.constants.UserRole;
import com.jangmo.web.constants.user.MemberStatus;
import com.jangmo.web.constants.user.MercenaryStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
public class UserListSearchRequest {

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Enumerated(EnumType.STRING)
    private MercenaryStatus mercenaryStatus;

    private String searchKeyword;

}
