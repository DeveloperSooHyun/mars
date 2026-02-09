package com.mars.web.core.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * JwtProperties.java
 *
 * application jwt 연동 class
 *
 * History
 * - 2026.02.09 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.09
 */

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * JWT 서명에 사용되는 시크릿 키
     */
    private String secret;

    /**
     * Access Token 만료 시간 (ms)
     */
    private long accessExpiration;

    /**
     * Refresh Token 만료 시간 (ms)
     */
    private long refreshExpiration;
}