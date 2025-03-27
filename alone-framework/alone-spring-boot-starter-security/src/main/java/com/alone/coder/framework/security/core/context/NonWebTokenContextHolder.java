
package com.alone.coder.framework.security.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

/**
 * 在非web 调用feign 传递之前手动set token
 */
@UtilityClass
public class NonWebTokenContextHolder {

	private static final ThreadLocal<String> THREAD_LOCAL_VERSION = new TransmittableThreadLocal<>();

	/**
	 * TTL 设置token<br/>
	 * @param token token
	 */
	public void setToken(String token) {
		THREAD_LOCAL_VERSION.set(token);
	}

	/**
	 * 获取TTL中的token
	 * @return token
	 */
	public String getToken() {
		return THREAD_LOCAL_VERSION.get();
	}

	/**
	 * 清除TTL中的token
	 */
	public void clear() {
		THREAD_LOCAL_VERSION.remove();
	}

}
