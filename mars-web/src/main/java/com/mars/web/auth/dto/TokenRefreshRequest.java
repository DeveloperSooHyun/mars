package com.mars.web.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * TokenRefreshRequest.java
 *
 * refresh 토큰으로 AccessToken 요청.
 *
 * History
 * - 2026.02.09 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.09
 */
@Getter
@NoArgsConstructor
public class TokenRefreshRequest {

    private String refreshToken;

}
