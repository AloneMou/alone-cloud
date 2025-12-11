package com.alone.coder.framework.portal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "aaa.portal")
public class PortalProperties {

    /**
     * 监听端口 50100
     */
    private Integer port = 50100;

    /**
     * 是否启用，默认启用
     */
    private Boolean enabled = true;


}
