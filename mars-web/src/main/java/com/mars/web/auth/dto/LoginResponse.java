package com.mars.web.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * LoginResponse.java
 *
 * 로그인 Response DTO.
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
public class LoginResponse {

    private String userId;
    private String accessToken;
    private String refreshToken;

}
