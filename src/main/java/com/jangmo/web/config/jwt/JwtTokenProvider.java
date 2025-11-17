package com.jangmo.web.config.jwt;

import com.jangmo.web.constants.UserRole;
import com.jangmo.web.security.ExtendedUserDetailsService;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final JwtConfig jwtConfig;

    private final ExtendedUserDetailsService userService;

    private final static String TOKEN_PREFIX = "Bearer ";

    public String create(String userAgent, String id, UserRole role) {
        Date issuedAt = new Date();
        Date expireAt = new Date(issuedAt.getTime() + jwtConfig.getValidTime().toMillis());
        return Jwts.builder()
                .subject(id)
                .claim("role", role.name())
                .claim("agent", userAgent)
                .issuedAt(issuedAt)
                .expiration(expireAt)
                .signWith(jwtConfig.getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String resolve(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }
        return token;
    }

    public void validateToken(String token) throws JwtException {
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build().parseSignedClaims(token);
        log.info("ExpiredAt :: {}", claims.getPayload().getExpiration());
    }

    public String getUserId(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userService.loadUserById(getUserId(token));
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    private UserDetails getDetail(String userId) {
        return userService.loadUserById(userId);
    }
}
