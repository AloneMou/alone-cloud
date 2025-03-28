
package com.alone.coder.framework.dynamic.route.configuration;

import com.alone.coder.framework.dynamic.route.filter.GrayReactiveLoadBalancerClientFilter;
import com.alone.coder.framework.dynamic.route.rule.GrayLoadBalancer;
import com.alone.coder.framework.dynamic.route.rule.VersionGrayLoadBalancer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.config.GatewayReactiveLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mica ribbon rule auto configuration.
 *
 * @author L.cm
 * @link https://github.com/lets-mica/mica
 */
@Configuration
@ConditionalOnProperty(value = "gray.rule.enabled", havingValue = "true")
@AutoConfigureBefore(GatewayReactiveLoadBalancerClientAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class GrayLoadBalancerClientConfiguration {

	@Bean
	public ReactiveLoadBalancerClientFilter gatewayLoadBalancerClientFilter(GrayLoadBalancer grayLoadBalancer,
																			LoadBalancerProperties properties, GatewayLoadBalancerProperties loadBalancerProperties) {
		return new GrayReactiveLoadBalancerClientFilter(loadBalancerProperties, properties, grayLoadBalancer);
	}

	@Bean
	public GrayLoadBalancer grayLoadBalancer(DiscoveryClient discoveryClient) {
		return new VersionGrayLoadBalancer(discoveryClient);
	}

}
