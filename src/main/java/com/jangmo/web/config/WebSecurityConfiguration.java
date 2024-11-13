package com.jangmo.web.config;

import com.jangmo.web.config.jwt.JwtAuthenticationFilter;
import com.jangmo.web.config.jwt.JwtTokenProvider;
import com.jangmo.web.security.ExtendedUserDetailsService;
import com.jangmo.web.security.ExtendedUserDetailServiceImpl;
import com.jangmo.web.service.UserService;
import com.jangmo.web.utils.BeanSuppliers;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final ApplicationContext context;

    public static final String[] PERMIT_ANT_PATH = {
            "/api/auth/**"
    };

    public static final String[] ADMIN_ANT_PATH = {
            "/api/admin/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests()
                .antMatchers(PERMIT_ANT_PATH).permitAll()
                .antMatchers(ADMIN_ANT_PATH).hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().cors()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(
                        new JwtAuthenticationFilter(
                                BeanSuppliers.beanSupplier(context, JwtTokenProvider.class)
                        ),
                        UsernamePasswordAuthenticationFilter.class
                ).build();
    }

    @Bean
    @Primary
    public ExtendedUserDetailsService userDetailsService() {
        return new ExtendedUserDetailServiceImpl(BeanSuppliers.beanSupplier(context, UserService.class));
    }

}
