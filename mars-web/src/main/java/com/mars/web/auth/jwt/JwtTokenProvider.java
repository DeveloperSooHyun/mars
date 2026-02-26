package com.mars.web.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
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
     * @param  userId , userTypeCd
     * @return String
     */
    public String createAccessToken(String userId, String userTypeCd) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtProperties.getAccessExpiration());

        return Jwts.builder()
                .subject(userId)
                .claim("userTypeCd", userTypeCd)
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
    	try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    
    /**
     * 토큰 예외처리
     *
     * @param  token
     * @return Boolean
     */
    private Claims getClaims(String jwt) {
    	try {
    	    Claims claims = Jwts.parser()
    	                        .verifyWith(getKey())
    	                        .build()
    	                        .parseSignedClaims(jwt)
    	                        .getPayload();
    	    return claims;
    	} catch (ExpiredJwtException e) {
            throw new JwtException("[ExpiredJwtException] 토큰이 만료되었습니다.");
        } catch (MalformedJwtException e) {
            throw new JwtException("[MalformedJwtException] 잘못된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("[UnsupportedJwtException] 잘못된 형식의 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new JwtException("[IllegalArgumentException] 잘못된 토큰입니다.");
        }
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
        String userTypeCd = claims.get("userTypeCd", String.class);

        User principal = new User(userId, "",
                List.of(new SimpleGrantedAuthority(userTypeCd)));

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
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    
    /**
     * 사용자 userTypeCd 조회
     *
     * @param  token
     * @return String
     */
    public String getRole(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.get("userTypeCd", String.class);
        } catch (JwtException e) {
            e.printStackTrace();
            return null;
        }
    }

    
    
    /**
     * 만료까지 남은 시간 조회
     *
     * @param  token
     * @return String
     */
    public long getExpiration(String token) {
    	try {
    		Claims claims = Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        	Date expiration = claims.getExpiration();
        	return expiration.getTime() - System.currentTimeMillis();
    	} catch (JwtException e) {
    		return 0L;
    	}
    }
    
    
    /**
     * HTTP 요청에서 Authorization 헤더에서 토큰 추출
     *
     * @param  token
     * @return String
     */
    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
