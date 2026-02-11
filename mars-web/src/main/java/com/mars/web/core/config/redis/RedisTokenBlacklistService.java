package com.mars.web.core.config.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * RedisTokenBlacklistService.java
 *
 * 로그아웃 시 JWT AccessToken 폐기 처리용
 *
 * History
 * - 2026.02.10 : 최초 생성.
 *
 * @author Mars
 * @since 2026.02.10
 */
@Component
public class RedisTokenBlacklistService implements RedisBlacklistService {
	private final StringRedisTemplate redisTemplate;
	
	public RedisTokenBlacklistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
	
	/**
     * 블랙리스트 지정
     *
     * @param  token, expiryMillis
     * @return
     */
	@Override
    public void setBlacklist(String token, long expiryMillis) {
        long ttl = (expiryMillis - System.currentTimeMillis()) / 1000;
        redisTemplate.opsForValue().set("BLACKLIST:" + token, "logout", ttl, TimeUnit.SECONDS);
    }

	/**
     * 블랙리스트 여부 확인
     *
     * @param  token
     * @return boolean
     */
    @Override
    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("BLACKLIST:" + token));
    }
}

