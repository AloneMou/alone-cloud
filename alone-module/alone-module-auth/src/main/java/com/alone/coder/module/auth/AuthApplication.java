
package com.alone.coder.module.auth;

import com.alone.coder.framework.feign.core.annotation.EnableAloneFeignClients;
import com.alone.coder.framework.security.core.annotation.EnableAloneResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

/**
 * @author lengleng
 * @date 2018年06月21日 认证授权中心
 */
@EnableAloneFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

}
