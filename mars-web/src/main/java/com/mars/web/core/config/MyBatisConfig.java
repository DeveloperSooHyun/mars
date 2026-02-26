package com.mars.web.core.config;

import java.util.LinkedHashMap;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mars.web.core.interceptor.SqlExecutionInterceptor;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatisConfig.java
 *
 * 클래스/기능 설명 작성
 *
 * History
 * - 2026.02.11 : 최초 생성
 *
 * @author Mars
 * @since 2026.02.11
 */
@Configuration
@MapperScan(
  basePackages = "com.mars.web",
  annotationClass = Mapper.class
)
public class MyBatisConfig {
	
	private final SqlExecutionInterceptor sqlExecutionInterceptor;
	
	public MyBatisConfig(SqlExecutionInterceptor sqlExecutionInterceptor) {
        this.sqlExecutionInterceptor = sqlExecutionInterceptor;
    }
	
	/**
     * 기존 interceptor 등록
     */
    @Bean
    Interceptor[] myBatisInterceptors() {
        return new Interceptor[]{sqlExecutionInterceptor};
    }

    /**
     * HMap alias 등록
     */
    @Bean
    org.apache.ibatis.session.Configuration myBatisConfiguration() {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.getTypeAliasRegistry().registerAlias("HMap", LinkedHashMap.class);
        return configuration;
    }
}
