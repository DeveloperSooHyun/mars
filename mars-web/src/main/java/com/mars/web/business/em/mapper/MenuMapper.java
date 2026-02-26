package com.mars.web.business.em.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * MenuMapper.java
 *
 * 메뉴 관련 Mapper.
 *
 * History
 * - 2026.02.20 : 최초 생성
 *
 * @author Mars
 * @since 2026.02.20
 */
@Mapper
public interface MenuMapper {
	// 사용자 정보 조회
	List<Map<String, Object>> selectMenuList(Map<String,Object> param);
}
