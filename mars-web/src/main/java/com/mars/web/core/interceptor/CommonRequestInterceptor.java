package com.mars.web.core.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.mars.web.core.constants.SecurityConstants;
import com.mars.web.core.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * CommonRequestInterceptor.java
 *
 * 업무 공통 파라미터 설정
 *
 * History
 * - 2026.02.11 : 최초 생성
 *
 * @author Mars
 * @since 2026.02.11
 */

@Component
public class CommonRequestInterceptor implements HandlerInterceptor {
    /**
     * Controller 이전에 사용자 정보 설정
     *
     * @param  
     * @return 
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) return true;

        String uri = request.getRequestURI();
        boolean isNoneCheckUrl = SecurityConstants.NONE_FILTER_CHECK_URL.stream().anyMatch(uri::contains);

        Map<String, Object> commonParams = new HashMap<>();
        
        if (!isNoneCheckUrl) {
	        // 공통 정보
	        commonParams.put("g_ip", CommonUtil.WebEx.clientIp(request));
	        commonParams.put("g_device", CommonUtil.WebEx.browser(request));
	
	        // JWT 사용자 정보
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	
	        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
	        	commonParams.put("g_userId", auth.getName());
	        	String userTypeCd = auth.getAuthorities()
	        	        .stream()
	        	        .findFirst()
	        	        .map(GrantedAuthority::getAuthority)
	        	        .orElse(null);
	        	commonParams.put("g_userTypeCd", userTypeCd);
	    	}
        }
        
        // 공통 파라미터 설정
        request.setAttribute("COMMON_PARAMS", commonParams);
        
        return true;
    }
}

