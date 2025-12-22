package com.alone.coder.framework.snmp.config;


import com.alone.coder.framework.snmp.core.connection.SnmpClientFactory;
import com.alone.coder.framework.snmp.core.connection.SnmpClientFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SnmpAutoConfiguration {

    @Bean
    public SnmpClientFactory snmpClientFactory() {
        return new SnmpClientFactoryImpl();
    }
}
