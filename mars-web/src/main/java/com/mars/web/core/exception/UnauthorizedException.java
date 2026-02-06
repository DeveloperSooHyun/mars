package com.mars.web.core.exception;

import com.mars.web.core.response.ApiResponseCode;

/**
 * UnauthorizedException.java
 *
 * 인증 및 권한 관련 예외 처리.
 *
 * History
 * - 2026.02.06 : 인증 로직 커스텀 예외 작성.
 *
 * @author Mars
 * @since 2026.02.06
 * @see ApiResponseCode
 */

public class UnauthorizedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final ApiResponseCode apiResponseCode;

    public UnauthorizedException(ApiResponseCode code) {
        super(code.getMessage());
        this.apiResponseCode = code;
    }

    public ApiResponseCode getApiResponseCode() {
        return apiResponseCode;
    }
}
