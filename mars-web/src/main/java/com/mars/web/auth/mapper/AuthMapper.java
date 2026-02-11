package com.mars.web.auth.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mars.web.auth.dto.LoginRequest;

/**
 * AuthMapper.java
 *
 * 로그인관련 Mapper.
 *
 * History
 * - 2026.02.09 : 최초 생성
 *
 * @author Mars
 * @since 2026.02.09
 */
@Mapper
public interface AuthMapper {
	// 사용자 정보 조회
	Map<String, Object> selectUserById(LoginRequest request);
	Map<String, Object> selectUserByToken(@Param("refreshToken") String refreshToken);
}
