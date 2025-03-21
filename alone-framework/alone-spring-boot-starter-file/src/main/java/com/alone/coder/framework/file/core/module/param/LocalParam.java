package com.alone.coder.framework.file.core.module.param;

import lombok.Getter;

/**
 * 本地存储初始化参数
 *
 * @author zhaojun
 */
@Getter
public class LocalParam extends ProxyDownloadParam {

	/**
	 * 文件路径
	 * <p>
	 * 只支持绝对路径<br>
	 * Docker 方式部署的话需提前映射宿主机路径！(<a class='link' target='_blank' href=
	 * 'https://docs.docker.com/engine/reference/run/#volume-shared-filesystems'>配置文档</a>)
	 * </p>
	 */
	private String filePath;

}