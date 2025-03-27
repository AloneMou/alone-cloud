
package com.alone.coder.framework.security.core.resolve;

import com.alone.coder.framework.security.config.PermitAllUrlProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

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

	/**
	 * 解析令牌
	 * @param request 请求
	 * @return {@link String }
	 */
	@Override
	public String resolve(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		String relativePath = requestUri.substring(request.getContextPath().length());
		if (urlProperties.isSkipPublicUrl()) {
			boolean match = urlProperties.getIgnoreUrls()
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
