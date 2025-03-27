package com.alone.coder.framework.security.core.handler;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import com.alone.coder.framework.common.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * <p>
 * 表单登录失败处理逻辑
 */
@Slf4j
public class FormAuthenticationFailureHandler implements AuthenticationFailureHandler {

	/**
	 * Called when an authentication attempt fails.
	 * @param request the request during which the authentication attempt occurred.
	 * @param response the response.
	 * @param exception the exception which was thrown to reject the authentication
	 */
	@Override
	@SneakyThrows
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) {
		log.debug("表单登录失败:{}", exception.getLocalizedMessage());
		String url = "/token/login?error=%s"
			.formatted(URLUtil.encode(exception.getMessage(), CharsetUtil.CHARSET_UTF_8));
		response.sendRedirect(url);
	}

}
