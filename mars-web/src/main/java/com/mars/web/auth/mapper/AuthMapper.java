package com.mars.web.auth.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

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
	Map<String, Object> selectUserById(String userId);
}
