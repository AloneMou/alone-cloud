package com.alone.coder.module.system;

import com.alone.coder.framework.feign.core.annotation.EnableAloneFeignClients;
import com.alone.coder.framework.security.core.annotation.EnableAloneResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAloneFeignClients
@EnableAloneResourceServer
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}
