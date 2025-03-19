package com.alone.coder.framework.file.core.service.impl;

import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;
import com.alone.coder.framework.file.core.module.param.AliYunParam;
import com.alone.coder.framework.file.core.service.base.AbstractS3BaseFileService;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * 阿里云 OSS 文件服务
 *
 * @author AgoniMou
 */
public class AliyunServiceImpl extends AbstractS3BaseFileService<AliYunParam> {

    public AliyunServiceImpl(String id, AliYunParam param) {
        super(id, param);
    }

    @Override
    public void init() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(param.getAccessKey(), param.getSecretKey());
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(param.getEndPoint(), "oss"))
                .build();
    }

    @Override
    public StorageTypeEnum getStorageTypeEnum() {
        return StorageTypeEnum.ALIYUN;
    }

}
