package com.alone.coder.framework.encrypt.config;

import com.alone.coder.framework.common.enums.WebFilterOrderEnum;
import com.alone.coder.framework.encrypt.core.filter.ApiEncryptFilter;
import com.alone.coder.framework.web.config.WebProperties;
import com.alone.coder.framework.web.core.handler.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static com.alone.coder.framework.web.config.YudaoWebAutoConfiguration.createFilterBean;

/**
 * HTTP API 加解密自动装配
 */
@AutoConfiguration
@Slf4j
@EnableConfigurationProperties(ApiEncryptProperties.class)
@ConditionalOnProperty(prefix = "yudao.api-encrypt", name = "enable", havingValue = "true")
public class AloneApiEncryptAutoConfiguration {

    /**
     * 创建 ApiEncryptFilter 过滤器
     *
     * @param webProperties                web 配置
     * @param apiEncryptProperties         加密配置
     * @param requestMappingHandlerMapping 请求映射
     * @param globalExceptionHandler       异常处理
     * @return 过滤器
     */
    @Bean
    public FilterRegistrationBean<ApiEncryptFilter> apiEncryptFilter(WebProperties webProperties,
                                                                     ApiEncryptProperties apiEncryptProperties,
                                                                     RequestMappingHandlerMapping requestMappingHandlerMapping,
                                                                     GlobalExceptionHandler globalExceptionHandler) {
        ApiEncryptFilter filter = new ApiEncryptFilter(webProperties, apiEncryptProperties,
                requestMappingHandlerMapping, globalExceptionHandler);
        return createFilterBean(filter, WebFilterOrderEnum.API_ENCRYPT_FILTER);

    }

}
