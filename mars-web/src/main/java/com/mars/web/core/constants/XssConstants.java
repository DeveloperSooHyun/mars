package com.mars.web.core.constants;

/**
 * SecurityConstants
 *
 * XSS 공격 방어를 위한 문자열 필터링 규칙 관리.
 *
 * History
 * - 2026.02.06 : 사용자 입력값에서 위험한 문자를 변환/삭제.
 *
 * @author Mars
 * @since 2026.02.06
 */
public class XssConstants {

    public static final String[][] CHECK_XSS = {
            {"<", "&lt;"},
            {">", "&gt;"},
            {"\\(", "&#40;"},
            {"\\)", "&#41;"}
    };
}
