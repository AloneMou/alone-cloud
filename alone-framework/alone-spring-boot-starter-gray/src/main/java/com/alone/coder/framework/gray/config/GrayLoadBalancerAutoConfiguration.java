package com.alone.coder.framework.gray.config;

import cn.hutool.core.util.StrUtil;
import com.alone.coder.framework.gray.core.chooser.IRuleChooser;
import com.alone.coder.framework.gray.core.chooser.RoundRuleChooser;
import com.alone.coder.framework.gray.core.interceptor.VersionFeignRequestInterceptor;
import com.alone.coder.framework.gray.core.rule.GrayRoundRobinLoadBalancer;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationTargetException;

/**
 *
 */
@Slf4j
@ConditionalOnProperty(value = "alone.gray.rule.enabled", matchIfMissing = true)
public class GrayLoadBalancerAutoConfiguration extends LoadBalancerClientConfiguration {

	private final String CONFIG_LOADBALANCE_ISOLATION = "alone.gray.rule.isolation";

	@Bean
	public RequestInterceptor versionFeignRequestInterceptor() {
		return new VersionFeignRequestInterceptor();
	}

	@Bean
	@ConditionalOnMissingBean(IRuleChooser.class)
	@ConditionalOnProperty(prefix = CONFIG_LOADBALANCE_ISOLATION, value = "chooser")
	public IRuleChooser customRuleChooser(Environment environment, ApplicationContext context) {
		IRuleChooser chooser = new RoundRuleChooser();
		String CONFIG_LOADBALANCE_ISOLATION_CHOOSER = CONFIG_LOADBALANCE_ISOLATION + ".chooser";
		if (environment.containsProperty(CONFIG_LOADBALANCE_ISOLATION_CHOOSER)) {
			String chooserRuleClassString = environment.getProperty(CONFIG_LOADBALANCE_ISOLATION_CHOOSER);
			if (StrUtil.isNotBlank(chooserRuleClassString)) {
				try {
					Class<?> ruleClass = ClassUtils.forName(chooserRuleClassString, context.getClassLoader());
					chooser = (IRuleChooser) ruleClass.getDeclaredConstructor().newInstance();
				}
				catch (ClassNotFoundException e) {
					log.error("没有找到定义的选择器，将使用内置的选择器", e);
				}
				catch (InstantiationException | IllegalAccessException | InvocationTargetException
						| NoSuchMethodException e) {
					log.error("没法创建定义的选择器，将使用内置的选择器", e);
				}
			}
		}
		return chooser;
	}

	@Bean
	@ConditionalOnMissingBean(value = IRuleChooser.class)
	public IRuleChooser defaultRuleChooser() {
		return new RoundRuleChooser();
	}

	@Bean
	@ConditionalOnMissingBean(value = IRuleChooser.class)
	public ReactorLoadBalancer<ServiceInstance> reactorServiceInstanceLoadBalancer(Environment environment,
			LoadBalancerClientFactory loadBalancerClientFactory, IRuleChooser chooser) {
		String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
		return new GrayRoundRobinLoadBalancer(
				loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name, chooser);
	}


}
