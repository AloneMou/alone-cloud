package com.alone.coder.framework.security.config;

import com.alone.coder.framework.security.core.auth.CustomOpaqueTokenIntrospector;
import com.alone.coder.framework.security.core.handler.AccessDeniedHandlerImpl;
import com.alone.coder.framework.security.core.handler.AuthenticationEntryPointImpl;
import com.alone.coder.framework.security.core.resolve.BearerTokenExtractor;
import com.alone.coder.framework.security.core.service.PermissionService;
import com.alone.coder.framework.security.core.service.PermissionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author lengleng
 */
@EnableConfigurationProperties(PermitAllUrlProperties.class)
public class AloneResourceServerAutoConfiguration {

	/**
	 * 鉴权具体的实现逻辑
	 * @return （#pms.xxx）
	 */
	@Bean("pms")
	public PermissionService permissionService() {
		return new PermissionServiceImpl();
	}

	/**
	 * 请求令牌的抽取逻辑
	 * @param urlProperties 对外暴露的接口列表
	 * @return BearerTokenExtractor
	 */
	@Bean
	public BearerTokenExtractor pigBearerTokenExtractor(PermitAllUrlProperties urlProperties) {
		return new BearerTokenExtractor(urlProperties);
	}

	/**
	 * 资源服务器异常处理
	 * @param securityMessageSource 自定义国际化处理器
	 * @return ResourceAuthExceptionEntryPoint
	 */
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint(MessageSource securityMessageSource) {
		return new AuthenticationEntryPointImpl(securityMessageSource);
	}

	/**
	 * 权限不够处理器 Bean
	 */
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new AccessDeniedHandlerImpl();
	}
//
//	/**
//	 * 资源服务器toke内省处理器
//	 * @param authorizationService token 存储实现
//	 * @return TokenIntrospector
//	 */
//	@Bean
//	public OpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2AuthorizationService authorizationService) {
//		return new CustomOpaqueTokenIntrospector(authorizationService);
//	}

}
