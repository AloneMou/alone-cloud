package com.alone.coder.framework.feign.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "alone.rest-template")
public class RestTemplateProperties {

	/**
	 * 最大链接数
	 */
	private int maxTotal = 200;

	/**
	 * 同路由最大并发数
	 */
	private int maxPerRoute = 50;

	/**
	 * 读取超时时间 ms
	 */
	private int readTimeout = 35000;

	/**
	 * 链接超时时间 ms
	 */
	private int connectTimeout = 10000;

}
