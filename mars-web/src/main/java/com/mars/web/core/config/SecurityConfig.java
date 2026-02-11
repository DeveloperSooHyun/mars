package com.mars.web.core.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mars.web.auth.jwt.JwtAuthenticationFilter;
import com.mars.web.auth.jwt.JwtTokenProvider;
import com.mars.web.core.config.redis.RedisBlacklistService;
import com.mars.web.core.constants.SecurityConstants;
import com.mars.web.core.interceptor.CommonRequestInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * SecurityConfig.java
 *
 * JWT 내용을 Spring Security 에 적용.
 *
 * History
 * - 2026.02.09 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.09
 */

@Configuration
public class SecurityConfig implements WebMvcConfigurer {
	private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
	
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisBlacklistService redisBlacklistService;
    private final CommonRequestInterceptor commonRequestInterceptor;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider,RedisBlacklistService redisBlacklistService,CommonRequestInterceptor commonRequestInterceptor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisBlacklistService = redisBlacklistService;
        this.commonRequestInterceptor = commonRequestInterceptor;
    }
    
    /**
     * Interceptor 등록 (공통파라미터 설정)
     *
     * @param  
     * @return 
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonRequestInterceptor)
                .addPathPatterns("/**");
    }
    
    /**
     * Security 공통 설정
     *
     * @param  
     * @return 
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	// 세션 로그인 제외
        	.csrf(csrf -> csrf.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            // 토큰 인증 선언
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 인증 허용 URL
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(SecurityConstants.NONE_FILTER_CHECK_URL.toArray(new String[0])).permitAll()
                .requestMatchers(SecurityConstants.PUBLIC_URL).permitAll()
                .anyRequest().authenticated()
            )
            // 예외 발생 응답 처리 
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(this::unauthorizedResponse)
                .accessDeniedHandler(this::accessDeniedResponse)
            )
            // JWT 필터 적용
            .addFilterBefore(
                new JwtAuthenticationFilter(jwtTokenProvider, redisBlacklistService),
                UsernamePasswordAuthenticationFilter.class
            )
            // iframe 허용 
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    /**
     * 토큰 누락, 만료
     *
     * @param  
     * @return 
     */
    private void unauthorizedResponse(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        
        log.warn("Unauthorized access: {}", request.getRequestURI());
        
        Map<String, Object> result = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "resultCd", 401,
                "error", "Unauthorized",
                "resultMsg", "인증에 실패했습니다.",
                "path", request.getRequestURI()
        );
        
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
        response.flushBuffer();
    }

    /**
     * 권한 접근 제한
     *
     * @param  
     * @return 
     */
    private void accessDeniedResponse(HttpServletRequest request, HttpServletResponse response, Exception accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        
        log.warn("Access denied: {}", request.getRequestURI());
        
        Map<String, Object> result = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "resultCd", 403,
                "error", "Forbidden",
                "resultMsg", "접근 권한이 없습니다.",
                "path", request.getRequestURI()
        );
        
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
        response.flushBuffer();
    }

    /**
     * Security 비밀번호 암호화
     *
     * @param  
     * @return 
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

