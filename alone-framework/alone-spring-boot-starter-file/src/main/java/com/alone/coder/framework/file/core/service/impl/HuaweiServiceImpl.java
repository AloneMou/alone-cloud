package com.alone.coder.framework.file.core.service.impl;

import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;
import com.alone.coder.framework.file.core.module.param.HuaweiParam;
import com.alone.coder.framework.file.core.service.base.AbstractS3BaseFileService;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

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
		BasicAWSCredentials credentials = new BasicAWSCredentials(param.getAccessKey(), param.getSecretKey());
		s3Client = AmazonS3ClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
			.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(param.getEndPoint(), "obs"))
			.build();
	}

	@Override
	public StorageTypeEnum getStorageTypeEnum() {
		return StorageTypeEnum.HUAWEI;
	}

}
