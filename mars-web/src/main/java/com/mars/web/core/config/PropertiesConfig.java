package com.mars.web.core.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.mars.web.auth.jwt.JwtProperties;

/**
 * PropertiesConfig.java
 *
 * cors, file, redis 관련 config 
 *
 * History
 * - 2026.02.09 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.09
 */

@Configuration
@EnableConfigurationProperties({
        JwtProperties.class
})
public class PropertiesConfig {
}
