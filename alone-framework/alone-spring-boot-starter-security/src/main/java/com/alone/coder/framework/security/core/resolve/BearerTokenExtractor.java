
package com.alone.coder.framework.security.core.resolve;

import cn.hutool.core.util.ReUtil;
import com.alone.coder.framework.common.util.spring.SpringUtils;
import com.alone.coder.framework.security.config.PermitAllUrlProperties;
import com.alone.coder.framework.security.core.annotation.Inner;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author caiqy
 * @date 2020.05.15
 */
@RequiredArgsConstructor
public class BearerTokenExtractor implements BearerTokenResolver {

	private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-:._~+/]+=*)$",
			Pattern.CASE_INSENSITIVE);

	private final PathMatcher pathMatcher = new AntPathMatcher();

	private final PermitAllUrlProperties urlProperties;


	private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");


	/**
	 * 解析令牌
	 * @param request 请求
	 * @return {@link String }
	 */
	@Override
	public String resolve(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		String relativePath = requestUri.substring(request.getContextPath().length());
		RequestMappingHandlerMapping mapping = SpringUtils.getBean("requestMappingHandlerMapping");
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

		List<String> ignoreUrls = urlProperties.getIgnoreUrls();
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
		if (urlProperties.isSkipPublicUrl()) {
			boolean match = ignoreUrls
				.stream()
				.anyMatch(url -> pathMatcher.match(url, relativePath));

			if (match) {
				return null;
			}
		}
		final String authorizationHeaderToken = resolveFromAuthorizationHeader(request);
		if (authorizationHeaderToken != null) {
			return authorizationHeaderToken;
		}
		return resolveFromRequestParameters(request);
	}

	/**
	 * 从授权标头解析
	 * @param request 请求
	 * @return {@link String }
	 */
	private String resolveFromAuthorizationHeader(HttpServletRequest request) {
		String BEARER_TOKEN_HEADER_NAME = HttpHeaders.AUTHORIZATION;
		String authorization = request.getHeader(BEARER_TOKEN_HEADER_NAME);
		if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
			return null;
		}
		Matcher matcher = authorizationPattern.matcher(authorization);
		if (!matcher.matches()) {
			BearerTokenError error = BearerTokenErrors.invalidToken("Bearer token is malformed");
			throw new OAuth2AuthenticationException(error);
		}
		return matcher.group("token");
	}

	/**
	 * 从请求参数解析
	 * @param request 请求
	 * @return {@link String }
	 */
	private static String resolveFromRequestParameters(HttpServletRequest request) {
		String[] values = request.getParameterValues("access_token");
		if (values == null || values.length == 0) {
			return null;
		}
		if (values.length == 1) {
			return values[0];
		}
		BearerTokenError error = BearerTokenErrors.invalidRequest("Found multiple bearer tokens in the request");
		throw new OAuth2AuthenticationException(error);
	}

}
