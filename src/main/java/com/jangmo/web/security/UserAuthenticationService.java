package com.jangmo.web.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserAuthenticationService {
    UserDetails loadUserById(String userId) throws UsernameNotFoundException;

    UserDetails loadUserByMobile(int mobile) throws UsernameNotFoundException;
}
