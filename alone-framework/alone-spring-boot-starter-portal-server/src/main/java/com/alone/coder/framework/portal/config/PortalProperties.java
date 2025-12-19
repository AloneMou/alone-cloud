package com.alone.coder.framework.portal.config;

import com.alone.coder.framework.portal.core.enums.PortalAuthTypeEnums;
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
    private Boolean enable = true;

    /**
     * Nas设备的默认端口
     */
    private Integer nasPort = 2000;

    /**
     * 认证方式 CHAP/PAP
     * 默认 CHAP
     */
    private PortalAuthTypeEnums authType = PortalAuthTypeEnums.AUTH_CHAP;

}
