package com.alone.coder.framework.portal.config;


import com.alone.coder.framework.portal.core.netty.NettyPortalServer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import jakarta.annotation.Resource;

@AutoConfiguration
@EnableConfigurationProperties(PortalProperties.class)
@ConditionalOnProperty(prefix = "aaa.portal", name = "enable", havingValue = "true", matchIfMissing = true) // 设置为 false 时，禁用
public class PortalConfig {

    @Resource
    private PortalProperties portalProperties;

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean
    public NettyPortalServer snmpTrapReceiver() {
        return new NettyPortalServer(portalProperties);
    }
}
