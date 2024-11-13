package com.jangmo.web.config.jwt;

import com.jangmo.web.config.WebSecurityConfiguration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Supplier;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final Supplier<JwtTokenProvider> jwtTokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        boolean requirePermission = Arrays.stream(WebSecurityConfiguration.PERMIT_ANT_PATH)
                .noneMatch(it -> matches(it, request.getRequestURI()));
        String token = jwtTokenProvider.get().resolve(request);
        if (requirePermission && token != null && jwtTokenProvider.get().isValid(token)) {
            Authentication authentication = jwtTokenProvider.get().getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private boolean matches(String pattern, String inputPath) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return pathMatcher.match(pattern, inputPath);
    }
}
