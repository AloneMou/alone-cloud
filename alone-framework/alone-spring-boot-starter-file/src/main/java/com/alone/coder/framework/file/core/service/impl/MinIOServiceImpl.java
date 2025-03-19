package com.alone.coder.framework.file.core.service.impl;

import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;
import com.alone.coder.framework.file.core.module.param.MinIOParam;
import com.alone.coder.framework.file.core.service.base.AbstractS3BaseFileService;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * Minio服务实现
 *
 * @author AgoniMou
 * @date 2024/12/10
 */
public class MinIOServiceImpl extends AbstractS3BaseFileService<MinIOParam> {


    public MinIOServiceImpl(String configId, MinIOParam param) {
        super(configId, param);
    }

    @Override
    public void init() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(param.getAccessKey(), param.getSecretKey());
        s3Client = AmazonS3ClientBuilder.standard()
                .withPathStyleAccessEnabled(true)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(param.getEndPoint(), "minio"))
                .build();
    }

    @Override
    public StorageTypeEnum getStorageTypeEnum() {
        return StorageTypeEnum.MINIO;
    }
}
