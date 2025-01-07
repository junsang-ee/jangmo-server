package com.jangmo.web.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface ExtendedUserDetailsService extends UserDetailsService {
    UserDetails loadUserById(String userId) throws UsernameNotFoundException;

    UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException;
}
