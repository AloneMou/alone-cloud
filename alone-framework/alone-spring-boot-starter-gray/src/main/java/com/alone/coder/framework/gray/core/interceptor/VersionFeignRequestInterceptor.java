package com.alone.coder.framework.gray.core.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alone.coder.framework.common.util.servlet.ServletUtils;
import com.alone.coder.framework.gray.core.constants.FeignCommonConstants;
import com.alone.coder.framework.gray.core.context.VersionContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VersionFeignRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		String reqVersion = ServletUtils.getRequest() != null
				? ServletUtils.getRequest().getHeader(FeignCommonConstants.VERSION) : VersionContextHolder.getVersion();
		if (StrUtil.isNotBlank(reqVersion)) {
			log.debug("feign gray add header version :{}", reqVersion);
			template.header(FeignCommonConstants.VERSION, reqVersion);
		}
	}

}
