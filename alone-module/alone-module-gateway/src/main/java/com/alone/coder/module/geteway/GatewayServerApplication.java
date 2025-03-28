package com.alone.coder.module.geteway;

import com.alone.coder.framework.dynamic.route.annotation.EnablePigxDynamicRoute;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnablePigxDynamicRoute
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}

}
