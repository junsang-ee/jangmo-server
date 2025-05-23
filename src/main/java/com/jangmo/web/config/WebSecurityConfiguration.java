package com.jangmo.web.config;

import com.jangmo.web.config.jwt.JwtAuthenticationEntryPoint;
import com.jangmo.web.config.jwt.JwtAuthenticationFilter;
import com.jangmo.web.config.jwt.JwtTokenProvider;
import com.jangmo.web.config.jwt.TestAccessDeninedException;
import com.jangmo.web.security.ExtendedUserDetailsService;
import com.jangmo.web.security.ExtendedUserDetailServiceImpl;
import com.jangmo.web.service.UserService;
import com.jangmo.web.utils.BeanSuppliers;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        order = AspectOrder.METHOD_SECURITY,
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final ApplicationContext context;

    private final JwtAuthenticationEntryPoint entryPoint;

    public static final String[] PERMIT_ANT_PATH = {
            "/api/auth/**"
    };

    public static final String[] ADMIN_ANT_PATH = {
            "/api/admin/**"
    };

    public static final String[] MANAGER_ANT_PATH = {
            "/api/managers/**"
    };

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests()
                .antMatchers(PERMIT_ANT_PATH).permitAll()
                .antMatchers(ADMIN_ANT_PATH).hasRole("ADMIN")
                .antMatchers(MANAGER_ANT_PATH).hasAnyRole("ADMIN", "MANAGER")
                .anyRequest().authenticated()
                .and().cors()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling()
                .authenticationEntryPoint(entryPoint)
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
