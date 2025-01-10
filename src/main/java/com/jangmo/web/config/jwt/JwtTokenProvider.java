package com.jangmo.web.config.jwt;

import com.jangmo.web.constants.UserRole;
import com.jangmo.web.security.ExtendedUserDetailsService;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final JwtConfig jwtConfig;

    private final ExtendedUserDetailsService userService;

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
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer")) {
            return token.substring(7);
        }
        return token;
    }

    public boolean isValid(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(jwtConfig.getSecretKey())
                    .build().parseSignedClaims(token);
            log.info("{}", claims.getPayload().getExpiration());
            return !claims.getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
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
