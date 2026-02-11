package com.mars.web.auth.jwt;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mars.web.core.config.redis.RedisBlacklistService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * JwtAuthenticationFilter.java
 *
 * 토큰 추출 및 인증 판단.
 *
 * History
 * - 2026.02.09 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.09
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisBlacklistService redisBlacklistService;

    public JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            RedisBlacklistService redisBlacklistService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisBlacklistService = redisBlacklistService;
    }

    /**
     * 토큰 검증, 블랙리스트 판단.
     *
     * @param  
     * @return 
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {

    	// 토큰 추출
        String token = jwtTokenProvider.resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {

            if (redisBlacklistService.isBlacklisted(token)) {
                throw new BadCredentialsException("Revoked token");
            }

            // 사용자 허용
            SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(token));
        }

        chain.doFilter(request, response);
    }

}