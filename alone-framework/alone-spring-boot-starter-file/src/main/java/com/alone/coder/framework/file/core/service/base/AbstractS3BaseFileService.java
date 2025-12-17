package com.alone.coder.framework.file.core.service.base;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.alone.coder.framework.common.util.string.StrUtils;
import com.alone.coder.framework.file.core.module.param.S3BaseParam;
import com.alone.coder.framework.file.core.service.impl.S3ServiceImpl;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Date;
import java.util.function.Consumer;

import static com.alone.coder.framework.file.core.service.base.AbstractProxyTransferService.DEFAULT_CONNECTION_TIMEOUT_SECONDS;

/**
 * s3 基础文件服务
 *
 * @author AgoniMou
 * @date 2024/12/10
 */
@Slf4j
public abstract class AbstractS3BaseFileService<P extends S3BaseParam> extends AbstractBaseFileService<P> {

	protected S3Client s3Client;

	protected S3Presigner s3Presigner;

	protected S3Presigner s3PresignerDownload;

	public static final InputStream EMPTY_INPUT_STREAM = new ByteArrayInputStream(new byte[0]);

	public AbstractS3BaseFileService(String configId, P param) {
		super(configId, param);
	}

	/**
	 * 默认 S3 获取对象下载链接的方法, 如果指定了域名, 则替换为自定义域名.
	 * @return S3 对象访问地址
	 */
	@Override
	public String getDownloadUrl(String pathAndName) {
		String bucketName = param.getBucketName();
		String domain = param.getDomain();
		String fullPath = StrUtils.concatTrimStartSlashes(param.getBasePath() + pathAndName);
		// 如果不是私有空间, 且指定了加速域名, 则直接返回下载地址.
		if (BooleanUtil.isFalse(param.isPrivate()) && StrUtil.isNotEmpty(domain)) {
			return StrUtils.concat(domain, StrUtils.encodeAllIgnoreSlashes(fullPath));
		}
		Integer tokenTime = param.getTokenTime();
		if (param.getTokenTime() == null || param.getTokenTime() < 1) {
			tokenTime = 1800;
		}

		GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				.applyMutation(processGeneratePresignedUrlRequest())
				.bucket(bucketName)
				.key(fullPath)
				.build();

		S3Presigner presigner = s3PresignerDownload != null ? s3PresignerDownload : s3Presigner;
		PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(GetObjectPresignRequest.builder()
				.getObjectRequest(getObjectRequest)
				.signatureDuration(Duration.ofSeconds(tokenTime))
				.build());

		URL url = presignedGetObjectRequest.url();
		String defaultUrl = url.toExternalForm();
		if (StringUtils.isNotEmpty(domain)) {
			String path = url.getFile();
			if (this instanceof S3ServiceImpl) {
				path = path.replaceFirst(bucketName + "/", "");
			}
			defaultUrl = StrUtils.concat(domain, path);
		}
		return defaultUrl;
	}

	@Override
	public String upload(byte[] content, String path, String type) {
		String year = DateUtil.format(new Date(), DatePattern.NORM_YEAR_PATTERN);
		String month = DateUtil.format(new Date(), "MM");
		String day = DateUtil.format(new Date(), "dd");
		// 拼接路径=> /2023/01/01/xxx.png
		path = StrUtils.concat(param.getBasePath(), year, "/", month, "/", day, "/", path);
		// 去除开头的 /
		path = StrUtils.trimStartSlashes(path);

		InputStream in = new ByteArrayInputStream(content);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(type);
		s3Client.putObject(param.getBucketName(), path, in, metadata);
		IoUtil.close(in);
		// 拼接返回路径
		return param.getDomain() + "/" + path;
	}

	@Override
	public boolean newFolder(String path, String name) {
		name = StrUtils.trimSlashes(name);
		String fullPath = StrUtils.concat(param.getBasePath(), path, name, StrUtils.DELIMITER_STR);
		fullPath = StrUtils.trimStartSlashes(fullPath);
		PutObjectRequest putObjectRequest = new PutObjectRequest(param.getBucketName(), fullPath, EMPTY_INPUT_STREAM,
				null);
		PutObjectResult putObjectResult = s3Client.putObject(putObjectRequest);
		return putObjectResult != null;
	}

	@Override
	public boolean deleteFile(String path) {
		String fullPath = StrUtils.concat(param.getBasePath(), path);
		fullPath = StrUtils.trimStartSlashes(fullPath);
		s3Client.deleteObject(param.getBucketName(), fullPath);
		return true;
	}

	@Override
	public byte[] getContent(String path) {
		String fullPath = StrUtils.concat(param.getBasePath(), path);
		fullPath = StrUtils.trimStartSlashes(fullPath);
		S3Object object = s3Client.getObject(param.getBucketName(), fullPath);
		return IoUtil.readBytes(object.getObjectContent());
	}


	public ClientOverrideConfiguration getClientConfiguration() {
		return ClientOverrideConfiguration.builder()
				.apiCallTimeout(Duration.ofSeconds(DEFAULT_CONNECTION_TIMEOUT_SECONDS)) // 设置 API 调用超时时间
				.build();
	}

	public Consumer<GetObjectRequest.Builder> processGeneratePresignedUrlRequest() {
		return builder -> {
		};
	}
}
