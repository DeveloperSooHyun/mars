package com.mars.web.core.constants;

import java.util.List;

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

    // API 호출이지만 Access Token 없을 때도 허용할 URL
    public static final List<String> NONE_FILTER_CHECK_URL = List.of(
        "/auth/login","/auth/logout"
    );
}

