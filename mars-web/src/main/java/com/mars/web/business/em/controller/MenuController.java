package com.mars.web.business.em.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mars.web.business.em.service.MenuService;
import com.mars.web.core.response.ApiResponse;

import lombok.RequiredArgsConstructor;

/**
 * MenuController.java
 *
 * 메뉴 관련 Controller.
 *
 * History
 * - 2026.02.26 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.26
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;
    
    /**
     * 사용자 별 메뉴 조회
     *
     * @param  paramMap
     * @return response
     */
    @PostMapping("/menuList")
    public ResponseEntity<ApiResponse<List<Map<String,Object>>>> selectMenuList(@RequestBody Map<String,Object> paramMap) {
    	return menuService.selectMenuList(paramMap);
    }

}
