package com.alone.coder.framework.file.core.service.impl;

import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;
import com.alone.coder.framework.file.core.module.param.TencentParam;
import com.alone.coder.framework.file.core.service.base.AbstractS3BaseFileService;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * 腾讯云 COS 存储实现
 *
 * @author AgoniMou
 */
public class TencentServiceImpl extends AbstractS3BaseFileService<TencentParam> {

	public TencentServiceImpl(String configId, TencentParam param) {
		super(configId, param);
	}

	@Override
	public void init() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(param.getAccessKey(), param.getSecretKey());
		s3Client = AmazonS3ClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
			.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(param.getEndPoint(), "cos"))
			.build();
	}

	@Override
	public StorageTypeEnum getStorageTypeEnum() {
		return StorageTypeEnum.TENCENT;
	}

}
