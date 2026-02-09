package com.mars.web.auth.jwt;

import com.mars.web.core.config.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

/**
 * JwtTokenProvider.java
 *
 * 토큰 생성 및 검증.
 *
 * History
 * - 2026.02.09 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.09
 */

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    /**
     * Access Token 생성
     *
     * @param  userId , role
     * @return String
     */
    public String createAccessToken(String userId, String role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.getAccessExpiration());

        return Jwts.builder()
                .subject(userId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getKey())
                .compact();
    }

    /**
     * Refresh Token 생성
     *
     * @param  userId
     * @return String
     */
    public String createRefreshToken(String userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.getRefreshExpiration());

        return Jwts.builder()
                .subject(userId)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getKey())
                .compact();
    }

    /**
     * 토큰 검증
     *
     * @param  token
     * @return Boolean
     */
    public boolean validateToken(String token) {
        Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token);
        return true;
    }

    /**
     * Authentication 생성
     *
     * @param  token
     * @return Authentication
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String userId = claims.getSubject();
        String role = claims.get("role", String.class);

        User principal = new User(userId, "",
                List.of(new SimpleGrantedAuthority(role)));

        return new UsernamePasswordAuthenticationToken(
                principal, "", principal.getAuthorities());
    }

    /**
     * 사용자 ID 조회
     *
     * @param  token
     * @return String
     */
    public String getUserId(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
