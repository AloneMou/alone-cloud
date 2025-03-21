package com.alone.coder.framework.file.core.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.UpYun;
import com.alone.coder.framework.common.util.string.StrUtils;
import com.alone.coder.framework.file.core.error.exception.InitializeStorageSourceException;
import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;
import com.alone.coder.framework.file.core.module.param.UpYunParam;
import com.alone.coder.framework.file.core.service.base.AbstractBaseFileService;
import com.upyun.Params;
import com.upyun.RestManager;
import com.upyun.UpException;
import com.upyun.UpYunUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.alone.coder.framework.file.core.error.code.StorageErrorCode.STORAGE_SOURCE_FILE_PROXY_UPLOAD_FAIL;

/**
 * @author zhaojun
 */
@Slf4j
public class UpYunServiceImpl extends AbstractBaseFileService<UpYunParam> {

	private static final String DELETE_NO_EMPTY_FOLDERS_MESSAGE = "directory not empty";

	private static final String END_MARK = "g2gCZAAEbmV4dGQAA2VvZg";

	private static RestManager restManager;

	public UpYunServiceImpl(String storageId, UpYunParam param) {
		super(storageId, param);
	}

	@Override
	public void init() {
		restManager = new RestManager(param.getBucketName(), param.getUsername(), param.getPassword());
	}

	@Override
	public String getDownloadUrl(String pathAndName) {
		String fullPath = StrUtils.concat(param.getBasePath(), pathAndName);

		String baseDownloadUrl = StrUtils.concat(param.getDomain(), StrUtils.encodeAllIgnoreSlashes(fullPath));
		// 判断是否配置了 token 防盗链.
		if (StrUtil.isNotEmpty(param.getToken())) {
			// 如果前面没有补 /, 则自动补 /, 不然生成的防盗链是无效的.
			long etime = System.currentTimeMillis() / 1000 + TimeUnit.MINUTES.toSeconds(param.getTokenTime());
			String downloadToken = SecureUtil.md5(param.getToken() + "&" + etime + "&" + fullPath).substring(12, 20);
			baseDownloadUrl += "?_upt=" + downloadToken + etime;
		}
		return baseDownloadUrl;
	}

	@Override
	public StorageTypeEnum getStorageTypeEnum() {
		return StorageTypeEnum.UPYUN;
	}

	@Override
	public String upload(byte[] content, String path, String type) {
		String year = DateUtil.format(new Date(), DatePattern.NORM_YEAR_PATTERN);
		String month = DateUtil.format(new Date(), "MM");
		String day = DateUtil.format(new Date(), "dd");
		// 拼接路径=> /2023/01/01/xxx.png
		path = StrUtils.concat(param.getBasePath(), year, "/", month, "/", day, "/", path);
		// 去除开头的 /
		path = StrUtils.removeDuplicateSlashes(path);
		try {
			restManager.writeFile(path, content, null);
		}
		catch (IOException | UpException e) {
			throw new InitializeStorageSourceException(STORAGE_SOURCE_FILE_PROXY_UPLOAD_FAIL);
		}
		// 拼接返回路径
		return param.getDomain() + "/" + path;
	}

	@Override
	public boolean newFolder(String path, String name) {
		String fullPath = StrUtils.concat(true, param.getBasePath(), path, name);
		try {
			Response response = restManager.mkDir(fullPath);
			return response.isSuccessful();
		}
		catch (IOException | UpException e) {
			throw ExceptionUtil.wrapRuntime(e);
		}
	}

	@Override
	public boolean deleteFile(String path) {
		String fullPath = StrUtils.concat(true, param.getBasePath(), path);
		try {
			Response response = restManager.deleteFile(fullPath, null);
			return response.isSuccessful();
		}
		catch (IOException | UpException e) {
			if (e instanceof UpException) {
				String message = e.getMessage();
				if (StrUtil.contains(message, DELETE_NO_EMPTY_FOLDERS_MESSAGE)) {
					throw new RuntimeException("非空文件夹不允许删除");
				}
			}
			throw ExceptionUtil.wrapRuntime(e);
		}
	}

	@Override
	public byte[] getContent(String path) throws Exception {
		Response response = restManager.readFile(path);
		assert response.body() != null;
		return response.body().bytes();
	}

}