package com.alone.coder.framework.security.config;

import cn.hutool.core.util.ReUtil;
import com.alone.coder.framework.common.util.spring.SpringUtils;
import com.alone.coder.framework.security.core.annotation.Inner;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 资源服务器对外直接暴露URL,如果设置contex-path 要特殊处理
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "security.oauth2.client")
public class PermitAllUrlProperties implements InitializingBean {

	private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

	private static final String[] DEFAULT_IGNORE_URLS = new String[] { "/actuator/**", "/error", "/v3/api-docs" };

	private List<String> ignoreUrls = new ArrayList<>();

	/**
	 * 跳过对外 URL
	 * <p>
	 * 比如某个接口是对外开放的，即使携带 token，也不鉴权
	 */
	private boolean skipPublicUrl = false;

	@Override
	public void afterPropertiesSet() {
		ignoreUrls.addAll(Arrays.asList(DEFAULT_IGNORE_URLS));
		RequestMappingHandlerMapping mapping = SpringUtils.getBean("requestMappingHandlerMapping");
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

		map.keySet().forEach(info -> {
			HandlerMethod handlerMethod = map.get(info);
			// 获取方法上边的注解 替代path variable 为 *
			Inner method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Inner.class);
			Optional.ofNullable(method)
				.ifPresent(inner -> Objects.requireNonNull(info.getPathPatternsCondition())
					.getPatternValues()
					.forEach(url -> ignoreUrls.add(ReUtil.replaceAll(url, PATTERN, "*"))));

			// 获取类上边的注解, 替代path variable 为 *
			Inner controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Inner.class);
			Optional.ofNullable(controller)
				.ifPresent(inner -> Objects.requireNonNull(info.getPathPatternsCondition())
					.getPatternValues()
					.forEach(url -> ignoreUrls.add(ReUtil.replaceAll(url, PATTERN, "*"))));
		});
	}

}
