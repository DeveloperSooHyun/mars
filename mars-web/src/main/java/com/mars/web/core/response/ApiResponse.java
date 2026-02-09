package com.mars.web.core.response;

import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


/**
 * ApiResponse
 *
 * API 응답 결과 리턴.
 *
 * History
 * - 2026.02.06 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.06
 */
@Getter
@Builder
@AllArgsConstructor
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
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        ApiResponseCode code = ApiResponseCode.SUCCESS;

        return ResponseEntity
                .status(code.getStatus())
                .body(ApiResponse.<T>builder()
                        .code(code.getCode())
                        .message(code.getMessage())
                        .data(data)
                        .build());
    }

    /**
     * ApiResponseCode를 기반으로 실패 객체를 반환한다.
     *
     * @param code 응답 코드
     * @return 실패 ApiResponse 객체
     */
    public static <T> ResponseEntity<ApiResponse<T>> fail(ApiResponseCode code) {
        return ResponseEntity
                .status(code.getStatus())
                .body(ApiResponse.<T>builder()
                        .code(code.getCode())
                        .message(code.getMessage())
                        .build());
    }
    
    /**
     * ApiResponseCode를 기반으로 실패 객체를 반환한다.
     *
     * @param code 응답 코드, message
     * @return 실패 ApiResponse 객체
     */
    public static <T> ResponseEntity<ApiResponse<T>> fail(ApiResponseCode code, String message) {
        return ResponseEntity
                .status(code.getStatus())
                .body(ApiResponse.<T>builder()
                        .code(code.getCode())
                        .message(message)
                        .build());
    }
}
