package com.alone.coder.framework.portal.config;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(PortalProperties.class)
@ConditionalOnProperty(prefix = "aaa.portal", name = "enable", havingValue = "true", matchIfMissing = true) // 设置为 false 时，禁用
public class PortalConfig {

}
