package com.mars.web.core.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * ApiResponseCode
 *
 * API 응답 결과에 대한 코드와 메시지 정의.
 *
 * History
 * - 2026.02.06 : 응답 코드 정의.
 *
 * @author Mars
 * @since 2026.02.06
 */
@Getter
@RequiredArgsConstructor
public enum ApiResponseCode {

	// 공통
    SUCCESS("0000", "성공하였습니다."),
    FAIL("9999", "실패하였습니다."),
    INTERNAL_ERROR("1004", "알 수 없는 오류가 발생하였습니다."),

    // 인증/인가
    UNAUTHORIZED("1001", "Unauthorized"),
    FORBIDDEN("1002", "권한이 부여되지 않은 사용자입니다."),
    NONE_AUTHORITY("2004", "권한이 부여되지 않은 사용자입니다."),

    // 사용자
    USER_NOT_FOUND("2001", "일치하는 사용자가 존재하지 않습니다."),
    PASSWORD_NOT_MATCH("2002", "비밀번호가 일치하지 않습니다."),
    USER_WHDWL_BEFORE("2005", "탈퇴한 사용자 입니다."),
    USER_APRV_BEFORE("2006", "미승인 사용자입니다."),

    // 토큰
    EXPIRED_REFRESH_TOKEN("3001", "토큰이 만료되었습니다."),
    UPD_USER_TOKEN_FAIL("3002", "토큰 생성에 실패하였습니다."),

    // OTP
    EXPIRED_OTP_TIME("4001", "OTP 인증 번호와 만료 시간을 확인해주세요."),

    // 파일
    FILE_READ_FAIL("5001", "해당 파일을 조회할 수 없습니다.");

    private final String code;
    private final String message;
}
