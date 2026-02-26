package com.mars.web.core.interceptor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import jakarta.servlet.http.HttpServletRequest;

/**
 * CommonRequestBodyAdvice.java
 *
 * 파라미터 공통 변수 설정
 *
 * History
 * - 2026.02.26 : 최초 생성
 *
 * @author Mars
 * @since 2026.02.26
 */
@RestControllerAdvice
public class CommonRequestBodyAdvice implements RequestBodyAdvice {
	
	@Override
    public boolean supports(MethodParameter methodParameter,
                            Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {

        // Map 타입일 때만 동작
        return Map.class.isAssignableFrom(methodParameter.getParameterType());
    }

	@Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage,MethodParameter parameter,Type targetType,Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		 return inputMessage;
    }
	 
	@SuppressWarnings("unchecked")
	@Override
	public Object afterBodyRead(Object body,
	                            HttpInputMessage inputMessage,
	                            MethodParameter parameter,
	                            Type targetType,
	                            Class<? extends HttpMessageConverter<?>> converterType) {

	    if (body instanceof Map) {
	        Map<String, Object> bodyMap = (Map<String, Object>) body;

	        // inputMessage 캐스팅 대신 RequestContextHolder 사용
	        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

	        Map<String, Object> commonParams = (Map<String, Object>) request.getAttribute("COMMON_PARAMS");

	        if (commonParams != null && !commonParams.isEmpty()) {
	            bodyMap.putAll(commonParams);
	        }
	    }

	    return body;
	}

	@SuppressWarnings("unchecked")
	@Override
    public Object handleEmptyBody(Object body,HttpInputMessage inputMessage,MethodParameter parameter,Type targetType,Class<? extends HttpMessageConverter<?>> converterType) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

	    Map<String, Object> commonParams = (Map<String, Object>) request.getAttribute("COMMON_PARAMS");

	    return commonParams != null ? new HashMap<>(commonParams) : new HashMap<>();
    }
	
}
