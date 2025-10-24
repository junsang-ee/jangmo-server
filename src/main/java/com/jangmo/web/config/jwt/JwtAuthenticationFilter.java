package com.jangmo.web.config.jwt;

import com.jangmo.web.config.WebSecurityConfiguration;
import com.jangmo.web.constants.message.ErrorMessage;
import com.jangmo.web.exception.AuthException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        try {
            if (requirePermission && token != null) {
                jwtTokenProvider.get().validateToken(token);
                Authentication authentication = jwtTokenProvider.get().getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch(ExpiredJwtException e) {
            reject(request, ErrorMessage.AUTH_EXPIRED);
        } catch(JwtException | IllegalArgumentException e) {
            reject(request, ErrorMessage.AUTH_INVALID);
        }

        chain.doFilter(request, response);
    }

    private boolean matches(String pattern, String inputPath) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return pathMatcher.match(pattern, inputPath);
    }

    private void reject(HttpServletRequest request, ErrorMessage error) {
        AuthException exception = new AuthException(error);
        request.setAttribute("exception", exception);
        throw exception;
    }
}
