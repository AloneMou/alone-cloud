package com.alone.coder.module.system.enums;


/**
 * API 相关的枚举
 *
 * @author 芋道源码
 */
public interface ApiConstants {

    /**
     * 服务名
     * <p>
     * 注意，需要保证和 spring.application.name 保持一致
     */
    String SERVE_NAME = "system-server";

    /**
     * 版本
     */
    String VERSION = "1.0.0";

    /**
     * 缓存前缀
     */
    String USER_DETAILS = "user_details";

}
