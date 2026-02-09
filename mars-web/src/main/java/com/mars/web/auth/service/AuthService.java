package com.mars.web.auth.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mars.web.auth.dto.LoginRequest;
import com.mars.web.auth.dto.LoginResponse;
import com.mars.web.auth.jwt.JwtTokenProvider;
import com.mars.web.auth.mapper.AuthMapper;
import com.mars.web.core.config.properties.JwtProperties;
import com.mars.web.core.exception.BusinessException;
import com.mars.web.core.response.ApiResponse;
import com.mars.web.core.response.ApiResponseCode;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * AuthService.java
 *
 * 로그인관련 Service.
 *
 * History
 * - 2026.02.09 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.09
 */

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    
    public ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request, HttpServletResponse response) {

		Map<String, Object> user = authMapper.selectUserById(request.getUserId());
		
		if (user == null) {
			throw new BusinessException(ApiResponseCode.USER_NOT_FOUND);
		}
		
		String dbPw = (String) user.get("USER_PW");
		
		if (!passwordEncoder.matches(request.getUserPw(), dbPw)) {
			throw new BusinessException(ApiResponseCode.PASSWORD_NOT_MATCH);
		}
		
		// userId, role 가져오기
		String userId = (String) user.get("USER_ID");
		String role = (String) user.get("USER_TYPE_CD");
		
		// JWT 생성
		String accessToken = jwtTokenProvider.createAccessToken(userId, role);
		String refreshToken = jwtTokenProvider.createRefreshToken(userId);
		
		// Refresh Token Cookie 설정
		Cookie cookie = new Cookie("refreshToken", refreshToken);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge((int) (jwtProperties.getRefreshExpiration() / 1000));
		response.addCookie(cookie);
		
		// Response DTO
		LoginResponse loginResponse = LoginResponse.builder()
		.userId(userId)
		.accessToken(accessToken)
		.refreshToken(refreshToken)
		.build();
		
		return ApiResponse.success(loginResponse);
	}


    public ResponseEntity<ApiResponse<String>> refresh(String refreshToken) {

        String userId = jwtTokenProvider.getUserId(refreshToken);
        String newAccessToken = jwtTokenProvider.createAccessToken(userId, "ROLE_USER");

        return ApiResponse.success(newAccessToken);
    }

    public ResponseEntity<ApiResponse<String>> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ApiResponse.success("logout");
    }
}
