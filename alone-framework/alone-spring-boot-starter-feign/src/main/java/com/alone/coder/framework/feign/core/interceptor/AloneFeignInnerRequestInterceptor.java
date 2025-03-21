package com.alone.coder.framework.feign.core.interceptor;

import com.alone.coder.framework.common.constant.SecurityConstants;
import com.alone.coder.framework.feign.core.annotation.NoToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

public class AloneFeignInnerRequestInterceptor implements RequestInterceptor, Ordered {

	@Override
	public void apply(RequestTemplate template) {
		Method method = template.methodMetadata().method();
		NoToken noToken = method.getAnnotation(NoToken.class);
		if (noToken != null) {
			template.header(SecurityConstants.FROM, SecurityConstants.FROM_IN);
		}
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}

}
