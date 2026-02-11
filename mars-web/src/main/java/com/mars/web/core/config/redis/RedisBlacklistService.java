package com.mars.web.core.config.redis;

/**
 * RedisBlacklistService.java
 *
 * 토큰 유효성 체크 인터페이스.
 *
 * History
 * - 2026.02.10 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.10
 */

public interface RedisBlacklistService {
	boolean isBlacklisted(String token);
	void setBlacklist(String token, long expireMills);
}
