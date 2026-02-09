package com.mars.web.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * TokenRefreshResponse.java
 *
 * refreshToken 으로 갱신된 accessToken 발급.
 *
 * History
 * - 2026.02.09 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.09
 */
@Getter
@Builder
@AllArgsConstructor
public class TokenRefreshResponse {

    private String accessToken;

}