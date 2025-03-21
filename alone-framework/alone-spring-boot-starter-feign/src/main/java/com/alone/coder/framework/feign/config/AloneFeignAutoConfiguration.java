package com.alone.coder.framework.feign.config;

import com.alone.coder.framework.feign.core.endpoint.FeignClientEndpoint;
import com.alone.coder.framework.feign.core.interceptor.AloneFeignInnerRequestInterceptor;
import feign.Feign;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.AloneFeignClientsRegistrar;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnClass(Feign.class)
@Import(AloneFeignClientsRegistrar.class)
@AutoConfigureAfter(EnableFeignClients.class)
public class AloneFeignAutoConfiguration {

	/**
	 * feign actuator endpoint
	 * @param context ApplicationContext
	 * @return FeignClientEndpoint
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnAvailableEndpoint
	public FeignClientEndpoint feignClientEndpoint(ApplicationContext context) {
		return new FeignClientEndpoint(context);
	}

	/**
	 * add inner request header 内部请求头添加器
	 * @return AloneFeignInnerRequestInterceptor
	 */
	@Bean
	public AloneFeignInnerRequestInterceptor pigFeignInnerRequestInterceptor() {
		return new AloneFeignInnerRequestInterceptor();
	}

}
