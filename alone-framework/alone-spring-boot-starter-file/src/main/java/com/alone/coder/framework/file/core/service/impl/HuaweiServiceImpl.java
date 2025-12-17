package com.alone.coder.framework.file.core.service.impl;

import com.alone.coder.framework.common.util.web.UrlUtils;
import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;
import com.alone.coder.framework.file.core.module.param.HuaweiParam;
import com.alone.coder.framework.file.core.service.base.AbstractS3BaseFileService;
import io.micrometer.common.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

/**
 * 华为云 OBS 存储服务实现
 *
 * @author AgoniMou
 */
public class HuaweiServiceImpl extends AbstractS3BaseFileService<HuaweiParam> {

	public HuaweiServiceImpl(String configId, HuaweiParam param) {
		super(configId, param);
	}

	@Override
	public void init() {
		String endPoint = param.getEndPoint();
		String endPointScheme = param.getEndPointScheme();
		// 如果 endPoint 不包含协议部分, 且配置了 endPointScheme, 则手动拼接协议部分.
		if (!UrlUtils.hasScheme(endPoint) && StringUtils.isNotBlank(endPointScheme)) {
			endPoint = endPointScheme + "://" + endPoint;
		}

		Region oss = Region.of("obs");
		URI endpointOverride = URI.create(endPoint);
		StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create(param.getAccessKey(), param.getSecretKey()));

		super.s3Client = S3Client.builder()
				.overrideConfiguration(getClientConfiguration())
				.region(oss)
				.endpointOverride(endpointOverride)
				.credentialsProvider(credentialsProvider)
				.build();

		super.s3Presigner = S3Presigner.builder()
				.region(oss)
				.endpointOverride(endpointOverride)
				.credentialsProvider(credentialsProvider)
				.build();

	}

	@Override
	public StorageTypeEnum getStorageTypeEnum() {
		return StorageTypeEnum.HUAWEI;
	}

}
