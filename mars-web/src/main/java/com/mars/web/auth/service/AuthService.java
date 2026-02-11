package com.mars.web.auth.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mars.web.auth.dto.LoginRequest;
import com.mars.web.auth.dto.LoginResponse;
import com.mars.web.auth.dto.TokenRefreshResponse;
import com.mars.web.auth.jwt.JwtProperties;
import com.mars.web.auth.jwt.JwtTokenProvider;
import com.mars.web.auth.mapper.AuthMapper;
import com.mars.web.core.config.redis.RedisBlacklistService;
import com.mars.web.core.exception.BusinessException;
import com.mars.web.core.response.ApiResponse;
import com.mars.web.core.response.ApiResponseCode;
import com.mars.web.core.util.CommonUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
    private final StringRedisTemplate redisTemplate;
    private final RedisBlacklistService redisBlacklistService;
    
    /**
     * 로그인
     *
     * @param  request, response
     * @return response
     */
    public ResponseEntity<ApiResponse<LoginResponse>> login(LoginRequest request, HttpServletResponse response) {
		Map<String, Object> user = authMapper.selectUserById(request);
		
		if (user == null || user.isEmpty()) {
			throw new BusinessException(ApiResponseCode.USER_NOT_FOUND);
		}
		
		String dbPw = CommonUtil.StringEx.nvl(user.get("USER_PW"));
		
		if (!passwordEncoder.matches(request.getUserPw(), dbPw)) {
			throw new BusinessException(ApiResponseCode.PASSWORD_NOT_MATCH);
		}
		
		// userId, role 가져오기
		String userId 	= CommonUtil.StringEx.nvl(user.get("USER_ID"));
		String role 	= CommonUtil.StringEx.nvl(user.get("USER_TYPE_CD"));
		
		// JWT 생성
		String accessToken 	= jwtTokenProvider.createAccessToken(userId, role);
		String refreshToken = jwtTokenProvider.createRefreshToken(userId);
		
		// 기존 세션 제거 (중복 로그인 방지)
		redisTemplate.delete("RT:" + userId);
		
		// Redis에 RefreshToken 저장
		redisTemplate.opsForValue().set(
            "RT:" + userId,
            refreshToken,
            jwtProperties.getRefreshExpiration(),
            TimeUnit.MILLISECONDS
        );
        
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


    /**
     * 토큰 갱신
     *
     * @param  refreshToken
     * @return response
     */
    public ResponseEntity<ApiResponse<TokenRefreshResponse>> refresh(String refreshToken) {
        // Redis 에 저장된 RefreshToken 조회
        String userId = jwtTokenProvider.getUserId(refreshToken);
        String savedToken = redisTemplate.opsForValue().get("RT:" + userId);
        
        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new BusinessException(ApiResponseCode.EXPIRED_REFRESH_TOKEN);
        }

        // AccessToken 발급
        String role = jwtTokenProvider.getRole(refreshToken);
        String newAccessToken = jwtTokenProvider.createAccessToken(userId, role);

        TokenRefreshResponse response = TokenRefreshResponse.builder()
                                                             .accessToken(newAccessToken)
                                                             .build();

        return ApiResponse.success(response);
    }


    /**
     * 로그아웃
     *
     * @param  response
     * @return response
     */
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request, HttpServletResponse response) {
        // AccessToken 추출
        String accessToken = jwtTokenProvider.resolveToken(request);
        
        if (accessToken == null) {
            throw new BusinessException(ApiResponseCode.UNAUTHORIZED);
        }

        // 사용자 ID 추출
        String userId = jwtTokenProvider.getUserId(accessToken);

        // Redis 에서 RefreshToken 삭제
        redisTemplate.delete("RT:" + userId);

        // AccessToken 블랙리스트 처리
        long expiration = jwtTokenProvider.getExpiration(accessToken);
        redisBlacklistService.setBlacklist(accessToken, System.currentTimeMillis() + expiration);

        // 쿠키 제거 (RefreshToken)
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ApiResponse.success("logout success");
    }


}
