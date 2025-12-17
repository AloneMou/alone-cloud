package com.alone.coder.framework.file.core.module.param;

import lombok.Data;
import lombok.Getter;

/**
 * 代理上传下载参数
 *
 * @author zhaojun
 */
@Data
public class ProxyTransferParam implements IStorageParam {

	/**
	 * 加速域名
	 * <p>
	 * 如不配置加速域名，则使用服务器中转下载, 反之则使用加速域名下载.
	 * </p>
	 */
	private String domain;

	/**
	 * 生成签名链接
	 * <p>
	 * 下载会生成带签名的下载链接, 如不想对外开放直链, 可以防止被当做直链使用.
	 * </p>
	 */
	private boolean isPrivate;

	/**
	 * 下载签名有效期
	 * <p>
	 * 用于下载签名的有效期, 单位为秒, 如不配置则默认为 1800 秒.
	 * </p>
	 */
	private Integer tokenTime;

}