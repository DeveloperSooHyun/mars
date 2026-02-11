package com.mars.web.core.util;

import java.util.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.ChronoUnit;
import java.nio.file.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.apache.commons.lang3.math.NumberUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseCookie;

/**
 * CommonUtil
 *
 * 공통 함수 
 *
 * History
 * - 2026.02.10 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.10
 */
public final class CommonUtil {

    private CommonUtil() {}

    /* =========================================================
     * String 관련
     * ========================================================= */
    public static final class StringEx {

        public static boolean hasText(String str) {
            return str != null && !str.trim().isEmpty();
        }

        public static String nvl(Object obj) {
            return Objects.toString(obj, "");
        }
    }


    /* =========================================================
     * Number 관련
     * ========================================================= */
    public static final class NumberEx {

        public static int toInt(Object obj) {
            return NumberUtils.toInt(StringEx.nvl(obj), 0);
        }

        public static long toLong(Object obj) {
            return NumberUtils.toLong(StringEx.nvl(obj), 0L);
        }

        public static double toDouble(Object obj) {
            return NumberUtils.toDouble(StringEx.nvl(obj), 0.0);
        }

        public static boolean isNumber(Object obj) {
            return NumberUtils.isCreatable(StringEx.nvl(obj));
        }
    }

    /* =========================================================
     * Random 관련
     * ========================================================= */
    public static final class RandomEx {

        public static String uuid() {
            return UUID.randomUUID().toString().replace("-", "");
        }

        public static String otp6() {
            return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        }
    }

    /* =========================================================
     * Date/Time 관련 (java.time)
     * ========================================================= */
    public static final class DateEx {

        public static String now(String pattern) {
            return format(LocalDateTime.now(), pattern);
        }

        public static String format(LocalDateTime time, String pattern) {
            return time.format(DateTimeFormatter.ofPattern(pattern));
        }

        public static String plus(String pattern, long amount, ChronoUnit unit) {
            return format(LocalDateTime.now().plus(amount, unit), pattern);
        }
    }

    /* =========================================================
     * Collection / Size 관련
     * ========================================================= */
    public static final class CollectionEx {

        public static int size(Object obj) {
            if (obj instanceof Collection<?> c) return c.size();
            if (obj instanceof Map<?, ?> m) return m.size();
            if (obj instanceof Object[] arr) return arr.length;
            if (obj instanceof String s) return s.length();
            return 0;
        }
    }

    /* =========================================================
     * Web 관련
     * ========================================================= */
    public static final class WebEx {

        public static Map<String, Object> getParameters(HttpServletRequest request) {
            return request.getParameterMap().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().length == 1 ? e.getValue()[0] : Arrays.asList(e.getValue())
                    ));
        }

        public static String clientIp(HttpServletRequest request) {
            return request.getRemoteAddr();
        }

        public static String browser(HttpServletRequest request) {
            String agent = request.getHeader("User-Agent");
            if (agent == null) return "Unknown";
            if (agent.contains("Chrome")) return "Chrome";
            if (agent.contains("Trident")) return "MSIE";
            if (agent.contains("Android")) return "Android";
            if (agent.contains("iPhone")) return "iPhone";
            return "Web";
        }
    }

    /* =========================================================
     * Security / Token / Cookie 관련
     * ========================================================= */
    public static final class SecurityEx {
    	
        public static String resolveRefreshToken(HttpServletRequest request) {
            if (request.getCookies() == null) return null;
            return Arrays.stream(request.getCookies())
                    .filter(c -> "refreshToken".equals(c.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        public static ResponseCookie refreshCookie(String token, long maxAge) {
            return ResponseCookie.from("refreshToken", token)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(maxAge)
                    .build();
        }
    }

    /* =========================================================
     * File Path 관련
     * ========================================================= */
    public static final class FileEx {

        public static String basePath(String base) {
            LocalDate now = LocalDate.now();
            return Paths.get(base,
                    String.valueOf(now.getYear()),
                    String.format("%02d", now.getMonthValue()),
                    String.format("%02d", now.getDayOfMonth())
            ).toString();
        }
    }

    /* =========================================================
     * Exception 관련
     * ========================================================= */
    public static final class ExceptionEx {

        public static String rootMessage(Throwable e) {
            Throwable root = e;
            while (root.getCause() != null) {
                root = root.getCause();
            }
            return Objects.toString(root.getMessage(), root.toString());
        }
    }
}
