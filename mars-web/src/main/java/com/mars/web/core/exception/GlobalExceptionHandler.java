package com.mars.web.core.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mars.web.core.response.ApiResponse;
import com.mars.web.core.response.ApiResponseCode;

/**
 * GlobalExceptionHandler.java
 *
 * 모든 컨트롤러에서 발생하는 예외 처리.
 *
 * History
 * - 2026.02.06 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.06
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
     * 비즈니스 예외 처리
     *
     * @param
     * @return fail ApiResponse 객체
     */
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException ex) {
	    return ApiResponse.fail(ex.getApiResponseCode(), ex.getMessage());
	}

    /**
     * 그 외 모든 예외 처리
     *
     * @param
     * @return fail ApiResponse 객체
     */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
	    ex.printStackTrace();
	    return ApiResponse.fail(ApiResponseCode.INTERNAL_ERROR);
	}
}
