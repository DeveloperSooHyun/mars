package com.mars.web.core.response;

import lombok.Builder;
import lombok.Getter;


/**
 * ApiResponse
 *
 * API 응답 결과 리턴.
 *
 * History
 * - 2026.02.06 : API 응답 결과 공통 생성.
 *
 * @author Mars
 * @since 2026.02.06
 */
@Getter
@Builder
public class ApiResponse<T> {

	private final String code;
    private final String message;
    private final T data;

    /**
     * ApiResponseCode를 기반으로 성공 객체를 반환한다.
     *
     * @param code 응답 코드
     * @return 성공 ApiResponse 객체
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(ApiResponseCode.SUCCESS.getCode())
                .message(ApiResponseCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    /**
     * ApiResponseCode를 기반으로 실패 객체를 반환한다.
     *
     * @param code 응답 코드
     * @return 실패 ApiResponse 객체
     */
    public static <T> ApiResponse<T> fail(ApiResponseCode code) {
        return ApiResponse.<T>builder()
                .code(code.getCode())
                .message(code.getMessage())
                .build();
    }
    
    /**
     * ApiResponseCode를 기반으로 실패 객체를 반환한다.
     *
     * @param code 응답 코드, message
     * @return 실패 ApiResponse 객체
     */
    public static <T> ApiResponse<T> fail(ApiResponseCode code, String message) {
        return ApiResponse.<T>builder()
                .code(code.getCode())
                .message(message)
                .build();
    }
}
