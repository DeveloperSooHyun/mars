package com.mars.web.auth.dto;

import java.util.List;
import java.util.Map;

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
    private String userTypeCd;
    
    private String accessToken;
    
    private List<Map<String,Object>> menus;

}
