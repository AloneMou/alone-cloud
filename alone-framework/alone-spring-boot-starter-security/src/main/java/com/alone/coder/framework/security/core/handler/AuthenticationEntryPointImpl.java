
package com.alone.coder.framework.security.core.handler;

import com.alone.coder.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.alone.coder.framework.common.pojo.CommonResult;
import com.alone.coder.framework.common.util.servlet.ServletUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import static com.alone.coder.framework.common.exception.enums.GlobalErrorCodeConstants.FAILED_DEPENDENCY;
import static com.alone.coder.framework.common.exception.enums.GlobalErrorCodeConstants.UNAUTHORIZED;

/**
 * 访问一个需要认证的 URL 资源，但是此时自己尚未认证（登录）的情况下，返回 {@link GlobalErrorCodeConstants#UNAUTHORIZED}
 * 错误码，从而使前端重定向到登录页
 * <p>
 * 补充：Spring Security 通过
 * {@link ExceptionTranslationFilter#sendStartAuthentication(HttpServletRequest, HttpServletResponse, FilterChain, AuthenticationException)}
 * 方法，调用当前类
 */
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("JavadocReference") // 忽略文档引用报错
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

	private final MessageSource messageSource;

	@Override
	@SneakyThrows
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) {
		CommonResult<String> result = CommonResult.error(UNAUTHORIZED);
		if (authException != null) {
			result.setMsg("error");
			result.setData(authException.getMessage());
			log.debug("[commence][访问 URL({}) 时，没有登录]", request.getRequestURI(), authException);
		}
		else {
			log.debug("[commence][访问 URL({}) 时，没有登录]", request.getRequestURI());
		}
		// 针对令牌过期返回特殊的 424
		if (authException instanceof InvalidBearerTokenException
				|| authException instanceof InsufficientAuthenticationException) {
			result.setCode(FAILED_DEPENDENCY.getCode());
			result.setMsg(this.messageSource.getMessage("OAuth2ResourceOwnerBaseAuthenticationProvider.tokenExpired",
					null, LocaleContextHolder.getLocale()));
		}
		// 返回 401
		ServletUtils.writeJSON(response, result);
	}

}
