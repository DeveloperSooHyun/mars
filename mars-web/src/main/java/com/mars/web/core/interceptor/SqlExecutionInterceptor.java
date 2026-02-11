package com.mars.web.core.interceptor;

import java.util.Arrays;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * SqlExecutionInterceptor.java
 *
 * 클래스/기능 설명 작성
 *
 * History
 * - 2026.02.11 : 최초 생성
 *
 * @author Mars
 * @since 2026.02.11
 */
@Component
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class SqlExecutionInterceptor implements Interceptor {

	private static final Logger log = LoggerFactory.getLogger(SqlExecutionInterceptor.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 특정 메서드에서만 로그 억제
        boolean suppress = Arrays.stream(Thread.currentThread().getStackTrace())
                .anyMatch(ste -> "schedulerExpiredQrCode".equals(ste.getMethodName()));
        if (suppress) {
            return invocation.proceed();
        }

        // SQL 및 파라미터 출력
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Object param = invocation.getArgs()[1];
        String sql = ms.getSqlSource().getBoundSql(param).getSql();
        log.info("SQL Statement: {}", sql);
        log.info("Parameters   : {}", toJson(param));

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * Object를 JSON 문자열로 변환, 변환 불가 시 toString 사용
     */
    private String toJson(Object obj) {
        if (obj == null) return "null";
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return obj.toString();
        }
    }
}
