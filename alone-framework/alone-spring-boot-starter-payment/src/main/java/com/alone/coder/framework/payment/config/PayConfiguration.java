package com.alone.coder.framework.payment.config;

import com.alone.coder.framework.payment.core.client.PayClientFactory;
import com.alone.coder.framework.payment.core.client.impl.PayClientFactoryImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(PayProperties.class)
public class PayConfiguration {

    @Bean
    public PayClientFactory payClientFactory() {
        return new PayClientFactoryImpl();
    }

}
