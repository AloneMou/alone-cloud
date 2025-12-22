package com.alone.coder.framework.snmp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "snmp.trap")
public class SnmpTrapProperties {

    /**
     * 端口
     */
    private int port = 162;

    /**
     * 线程池大小
     */
    private int threadPoolSize = 4;


    private TrapMode mode = TrapMode.NETTY;

    public enum TrapMode { NETTY, SNMP4J }
}
