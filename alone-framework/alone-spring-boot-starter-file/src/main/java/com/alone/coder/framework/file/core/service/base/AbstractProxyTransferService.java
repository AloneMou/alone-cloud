package com.alone.coder.framework.file.core.service.base;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.StrUtil;
import com.alone.coder.framework.common.util.string.StrUtils;
import com.alone.coder.framework.file.core.module.param.ProxyTransferParam;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;

import java.io.InputStream;

/**
 * 代理传输数据(上传/下载) Service
 *
 * @author zhaojun
 */
public abstract class AbstractProxyTransferService<P extends ProxyTransferParam> extends AbstractBaseFileService<P>{



	/**
	 * 服务器代理下载 URL 前缀.
	 */
	public static final String PROXY_DOWNLOAD_LINK_PREFIX = "/pd";


	/**
	 * 服务器代理下载 URL 前缀.
	 */
	public static final String PROXY_UPLOAD_LINK_PREFIX = "/file/upload";

	public AbstractProxyTransferService(String storageId, P param) {
		super(storageId, param);
	}

}