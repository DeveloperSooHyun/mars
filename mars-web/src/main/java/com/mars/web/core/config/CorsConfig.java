package com.mars.web.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CorsConfig.java
 *
 * vue 와 백엔드 포트 CORS 허용 
 *
 * History
 * - 2026.02.12 : 최초 생성
 *
 * @author Mars
 * @since 2026.02.12
 */
@Configuration
public class CorsConfig {
    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:18010") // vue 개발 서버 
                        .allowedMethods("*");
            }
        };
    }
}

