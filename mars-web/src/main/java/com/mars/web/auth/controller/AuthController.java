package com.mars.web.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mars.web.auth.dto.LoginRequest;
import com.mars.web.auth.dto.LoginResponse;
import com.mars.web.auth.dto.TokenRefreshResponse;
import com.mars.web.auth.service.AuthService;
import com.mars.web.core.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * AuthController.java
 *
 * 로그인관련 Controller.
 *
 * History
 * - 2026.02.09 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.09
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    
    /**
     * 로그인
     *
     * @param  request, response
     * @return response
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request, HttpServletResponse response) {
    	return authService.login(request, response);
    }

    /**
     * 토큰 갱신
     *
     * @param  refreshToken(HttpOnly Cookie 자동 추출)
     * @return response
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenRefreshResponse>> refresh(@CookieValue("refreshToken") String refreshToken) {
    	return authService.refresh(refreshToken);
    }

    /**
     * 로그아웃
     *
     * @param  response
     * @return response
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request, HttpServletResponse response) {
        return authService.logout(request, response);
    }

}
