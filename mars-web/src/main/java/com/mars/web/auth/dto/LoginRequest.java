package com.mars.web.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * LoginRequest.java
 *
 * 로그인 Request DTO.
 *
 * History
 * - 2026.02.09 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.09
 */
@Getter
@NoArgsConstructor
public class LoginRequest {

    private String userId;
    private String userPw;

}
