package com.mars.web.business.em.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mars.web.business.em.mapper.MenuMapper;
import com.mars.web.core.response.ApiResponse;

import lombok.RequiredArgsConstructor;

/**
 * MenuService.java
 *
 * 메뉴 관련 Service.
 *
 * History
 * - 2026.02.26 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.26
 */

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuMapper menuMapper;
    
    /**
     * 사용자 별 메뉴 조회
     *
     * @param  paramMap
     * @return response
     */
    public ResponseEntity<ApiResponse<List<Map<String,Object>>>> selectMenuList(Map<String,Object> paramMap) {
    	List<Map<String,Object>> menuList = menuMapper.selectMenuList(paramMap);
    	
		return ApiResponse.success(menuList);
	}


}
