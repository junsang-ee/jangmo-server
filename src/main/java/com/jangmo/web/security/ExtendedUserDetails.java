package com.jangmo.web.security;

import com.jangmo.web.constants.UserRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ExtendedUserDetails extends User {
    private final String id;

    public ExtendedUserDetails(String id, int phoneNumber, UserRole role){
        super(String.valueOf(phoneNumber), "", AuthorityUtils.createAuthorityList("ROLE_" + role.name()));
        this.id = id;
    }

    public ExtendedUserDetails(String id, int phoneNumber, UserRole role, boolean enabled){
        super(String.valueOf(phoneNumber), "", enabled, true, true, true,
                AuthorityUtils.createAuthorityList("ROLE_" + role.name()));
        this.id = id;
    }
}