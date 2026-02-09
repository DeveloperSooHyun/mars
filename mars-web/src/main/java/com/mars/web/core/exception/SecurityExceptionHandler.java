package com.mars.web.core.exception;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mars.web.core.response.ApiResponse;
import com.mars.web.core.response.ApiResponseCode;

import jakarta.servlet.http.HttpServletResponse;

/**
 * SecurityExceptionHandler.java
 *
 * JWT / Security 예외 처리 
 *
 * History
 * - 2026.02.09 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.09
 */

@Component
public class SecurityExceptionHandler {

    public void handle(HttpServletResponse response, ApiResponseCode code) throws IOException {
        response.setStatus(code.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<Object> body = ApiResponse.builder()
                .code(code.getCode())
                .message(code.getMessage())
                .build();

        new ObjectMapper().writeValue(response.getWriter(), body);
    }
}

