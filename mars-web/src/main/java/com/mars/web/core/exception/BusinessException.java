package com.mars.web.core.exception;

import com.mars.web.core.response.ApiResponseCode;

/**
 * BusinessException.java
 *
 * 업무 로직에서 발생하는 예외 처리.
 *
 * History
 * - 2026.02.06 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.06
 * @see ApiResponseCode
 */
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final ApiResponseCode apiResponseCode;
	
	public BusinessException(ApiResponseCode code) {
		super(code.getMessage());
		this.apiResponseCode = code;
	}
	
	public BusinessException(ApiResponseCode code, String message) {
        super(message);
        this.apiResponseCode = code;
    }

    public ApiResponseCode getApiResponseCode() {
        return apiResponseCode;
    }

}
