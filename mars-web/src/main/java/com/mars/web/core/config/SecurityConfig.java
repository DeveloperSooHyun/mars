package com.mars.web.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.mars.web.auth.jwt.JwtAuthenticationFilter;
import com.mars.web.auth.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // CSRF 비활성화 (JWT 사용 시 필수)
            .csrf(csrf -> csrf.disable())

            // 세션 사용 안 함 (JWT)
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // URL 권한 설정
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/health").permitAll()
                    .anyRequest().authenticated()
            )

            // JWT 필터 등록
            .addFilterBefore(
                    new JwtAuthenticationFilter(jwtTokenProvider),
                    org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
            )

            // 기본 로그인 폼 비활성화
            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
