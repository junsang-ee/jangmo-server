package com.jangmo.web.config;

import com.jangmo.web.config.jwt.JwtAccessDeniedHandler;
import com.jangmo.web.config.jwt.JwtAuthenticationEntryPoint;
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
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
	securedEnabled = true,
	jsr250Enabled = true,
	prePostEnabled = true
)
@RequiredArgsConstructor
public class WebSecurityConfiguration {

	private final ApplicationContext context;

	private final JwtAuthenticationEntryPoint entryPoint;

	private final JwtAccessDeniedHandler accessDeniedHandler;

	public static final String[] PERMIT_ANT_PATH = {
		"/api/auth/**",
		"/api/locations/**"
	};

	public static final String[] AUTHENTICATED_ANT_PATH = {
		"/api/matches/**"
	};

	public static final String[] ADMIN_ANT_PATH = {
		"/api/admin/**"
	};

	public static final String[] MANAGER_ANT_PATH = {
		"/api/managers/**"
	};

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(AbstractHttpConfigurer::disable)
			.cors(Customizer.withDefaults())
			.sessionManagement(session ->
					session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			).exceptionHandling(
				exceptions -> exceptions
					.authenticationEntryPoint(entryPoint)
					.accessDeniedHandler(accessDeniedHandler)
			).authorizeHttpRequests(
				auth -> auth
					.requestMatchers(PERMIT_ANT_PATH).permitAll()
					.requestMatchers(AUTHENTICATED_ANT_PATH).authenticated()
					.requestMatchers(ADMIN_ANT_PATH).hasRole("ADMIN")
					.requestMatchers(MANAGER_ANT_PATH).hasAnyRole("ADMIN", "MANAGER")
					.anyRequest().authenticated()
			).addFilterBefore(
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
