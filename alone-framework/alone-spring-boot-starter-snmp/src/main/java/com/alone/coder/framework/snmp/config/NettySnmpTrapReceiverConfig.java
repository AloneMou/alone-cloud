package com.alone.coder.framework.snmp.config;


import com.alone.coder.framework.snmp.core.service.SnmpSettingApi;
import com.alone.coder.framework.snmp.core.trap.SnmpTrapReceiver;
import com.alone.coder.framework.snmp.core.trap.TrapHandler;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ThreadPoolExecutor;

@AutoConfiguration
@EnableConfigurationProperties(SnmpTrapProperties.class)
public class NettySnmpTrapReceiverConfig {

    @Resource
    private SnmpTrapProperties snmpTrapProperties;


    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean
    public SnmpTrapReceiver snmpTrapReceiver(TrapHandler trapHandler) {
        return new SnmpTrapReceiver(snmpTrapProperties, trapHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public TrapHandler trapHandler(SnmpSettingApi snmpSettingApi, ThreadPoolExecutor trapSyncExecutor) {
        return new TrapHandler(trapSyncExecutor, snmpSettingApi);
    }
}
