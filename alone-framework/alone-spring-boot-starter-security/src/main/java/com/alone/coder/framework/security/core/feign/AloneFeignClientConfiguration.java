package com.alone.coder.framework.security.core.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

public class AloneFeignClientConfiguration {

	/**
	 * 注入 oauth2 feign token 增强
	 * @param tokenResolver token获取处理器
	 * @return 拦截器
	 */
	@Bean
	public RequestInterceptor oauthRequestInterceptor(BearerTokenResolver tokenResolver) {
		return new AloneOAuthRequestInterceptor(tokenResolver);
	}

}
