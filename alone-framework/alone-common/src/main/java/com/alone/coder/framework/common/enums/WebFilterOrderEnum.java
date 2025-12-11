package com.alone.coder.framework.common.enums;

/**
 * Web 过滤器顺序的枚举类，保证过滤器按照符合我们的预期
 * <p>
 * 考虑到每个 starter 都需要用到该工具类，所以放到 common 模块下的 enum 包下
 *
 * @author 芋道源码
 */
public interface WebFilterOrderEnum {
    /**
     * CORS 跨域过滤器
     */
    int CORS_FILTER = Integer.MIN_VALUE;

    /**
     * TraceId 过滤器
     */
    int TRACE_FILTER = CORS_FILTER + 1;

    /**
     * 环境标识过滤器
     */
    int ENV_TAG_FILTER = TRACE_FILTER + 1;

    /**
     * 请求缓存过滤器
     */
    int REQUEST_BODY_CACHE_FILTER = Integer.MIN_VALUE + 500;

    /**
     * 加密过滤器
     */
    int API_ENCRYPT_FILTER = REQUEST_BODY_CACHE_FILTER + 1;
    // OrderedRequestContextFilter 默认为 -105，用于国际化上下文等等

    /**
     * 多租户过滤器
     */
    int TENANT_CONTEXT_FILTER = -104; // 需要保证在 ApiAccessLogFilter 前面

    /**
     * API 访问日志过滤器
     */
    int API_ACCESS_LOG_FILTER = -103; // 需要保证在 RequestBodyCacheFilter 后面

    /**
     * XSS 过滤器
     */
    int XSS_FILTER = -102; // 需要保证在 RequestBodyCacheFilter 后面

    // Spring Security Filter 默认为 -100，可见
    // org.springframework.boot.autoconfigure.security.SecurityProperties 配置属性类

    /**
     * 多租户过滤器
     */
    int TENANT_SECURITY_FILTER = -99; // 需要保证在 Spring Security 过滤器后面

    /**
     * Flowable 过滤器
     */
    int FLOWABLE_FILTER = -98; // 需要保证在 Spring Security 过滤后面

    /**
     * 演示环境过滤器
     */
    int DEMO_FILTER = Integer.MAX_VALUE;


}
