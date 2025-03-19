package com.alone.coder.framework.file.core.module.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * S3 初始化参数
 *
 * @author zhaojun
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class S3Param extends S3BaseParam {

	/**
	 * EndPoint
	 */
	private String endPoint;

	/**
	 * 地域
	 */
	private String region;

	/**
	 * 域名风格
	 * <a href="https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/VirtualHosting.html#path-style-access">查看 S3 API 说明文档</a>
	 */
	private String pathStyle;

}