package com.alone.coder.module.auth.support.handler;

import cn.hutool.core.util.StrUtil;
import com.alone.coder.framework.common.pojo.CommonResult;
import com.alone.coder.module.system.api.user.AdminUserApi;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lengleng
 * @date 2022-06-02
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PigxAuthenticationFailureEventHandler implements AuthenticationFailureHandler {

    private static final MappingJackson2HttpMessageConverter errorHttpResponseConverter = new MappingJackson2HttpMessageConverter();

    private final StringRedisTemplate redisTemplate;

//    private final AdminUserApi adminUserApi;

    /**
     * 登录错误次数
     */
    String LOGIN_ERROR_TIMES = "login_error_times";

    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     *                  request.
     */
    @Override
    @SneakyThrows
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) {
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        log.info("登录失败，异常：{}", exception.getLocalizedMessage());

        // 密码模式记录错误信息
        if (OAuth2ParameterNames.PASSWORD.equals(grantType)) {
            String username = request.getParameter(OAuth2ParameterNames.USERNAME);
            if (exception instanceof OAuth2AuthenticationException) {
                recordLoginFailureTimes(username);
            }
            // 记录登录失败错误信息
            sendFailureEventLog(request, exception, username);
        }

        // 写出错误信息
        sendErrorResponse(request, response, exception);
    }

    /**
     * 记录失败日志
     *
     * @param request   HttpServletRequest
     * @param exception 错误日志
     * @param username  用户名
     */
    private void sendFailureEventLog(HttpServletRequest request, AuthenticationException exception, String username) {
//		SysLogDTO logVo = SysLogUtils.getSysLog();
//		logVo.setTitle("登录失败");
//		logVo.setLogType(LogTypeEnum.ERROR.getType());
//		logVo.setException(exception.getLocalizedMessage());
//		// 发送异步日志事件
//		String startTimeStr = request.getHeader(CommonConstants.REQUEST_START_TIME);
//		if (StrUtil.isNotBlank(startTimeStr)) {
//			Long startTime = Long.parseLong(startTimeStr);
//			Long endTime = System.currentTimeMillis();
//			logVo.setTime(endTime - startTime);
//		}
//		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
//		String clientId = WebUtils.extractClientId(header).orElse(null);
//		logVo.setServiceId(clientId);
//		logVo.setCreateBy(username);
//		logVo.setTenantId(Long.parseLong(tenantKeyStrResolver.key()));
//		publisher.publishEvent(new SysLogEvent(logVo));
    }

    /**
     * 记录登录失败此处，如果操作阈值 调用接口锁定用户
     *
     * @param username username
     */
    private void recordLoginFailureTimes(String username) {
//		String key = "%s:%s".formatted(LOGIN_ERROR_TIMES, username);
//		Long deltaTimes = ParamResolver.getLong("LOGIN_ERROR_TIMES", 5L);
//		Long times = redisTemplate.opsForValue().increment(key);
//
//		// 自动过期时间
//		Long deltaTime = ParamResolver.getLong("DELTA_TIME", 1L);
//		redisTemplate.expire(key, deltaTime, TimeUnit.HOURS);
//
//		if (deltaTimes <= times) {
//			userService.lockUser(username);
//		}
    }

    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response,
                                   AuthenticationException exception) throws IOException {
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        String errorMessage;

        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException authorizationException = (OAuth2AuthenticationException) exception;
            errorMessage = StrUtil.isBlank(authorizationException.getError().getDescription())
                    ? authorizationException.getError().getErrorCode()
                    : authorizationException.getError().getDescription();

            errorHttpResponseConverter.write(
                    CommonResult.error(500, authorizationException.getError().getErrorCode() + ":" + errorMessage),
                    MediaType.APPLICATION_JSON, httpResponse);

        } else {
            errorMessage = exception.getLocalizedMessage();
            errorHttpResponseConverter.write(CommonResult.error(500, errorMessage), MediaType.APPLICATION_JSON, httpResponse);
        }
    }

}
