package com.mars.web.core.constants;

/**
 * SecurityConstants
 *
 * JWT, 토큰, 접근 제어 관련 상수 관리.
 *
 * History
 * - 2026.02.06 : AccessToken/RefreshToken 만료 시간, Security 통과 URL 등 설정
 *
 * @author Mars
 * @since 2026.02.06
 */
public class SecurityConstants {

    public static final String[] PUBLIC_URL = {
            "/", "/index.html", "/css/**", "/js/**", "/images/**"
    };

    public static final int ACCESS_TOKEN_EXPIRED = 30 * 60;
    public static final int REFRESH_TOKEN_EXPIRED = 7 * 24 * 60 * 60;
}

