
package com.alone.coder.module.auth.support.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * @author zhangran
 * @date 2022-06-02
 *
 * 事件机制处理退出相关
 */
@Slf4j
@Component
public class AloneLogoutSuccessEventHandler implements ApplicationListener<LogoutSuccessEvent> {

	@Override
	public void onApplicationEvent(LogoutSuccessEvent event) {
		Authentication authentication = (Authentication) event.getSource();
		if (authentication instanceof PreAuthenticatedAuthenticationToken) {
			handle(authentication);
		}
	}

	/**
	 * 处理退出成功方法
	 * <p>
	 * 获取到登录的authentication 对象
	 * @param authentication 登录对象
	 */
	public void handle(Authentication authentication) {
//		log.info("用户：{} 退出成功", authentication.getPrincipal());
//		SysLogDTO logVo = SysLogUtils.getSysLog();
//		logVo.setTitle("退出成功");
//		// 发送异步日志事件
//		Long startTime = System.currentTimeMillis();
//		Long endTime = System.currentTimeMillis();
//		logVo.setTime(endTime - startTime);
//
//		// 设置对应的token
//		logVo.setParams(WebUtils.getRequest().getHeader(HttpHeaders.AUTHORIZATION));
//
//		// 这边设置ServiceId
//		if (authentication instanceof PreAuthenticatedAuthenticationToken) {
//			logVo.setServiceId(authentication.getCredentials().toString());
//		}
//		logVo.setCreateBy(authentication.getName());
	}

}
