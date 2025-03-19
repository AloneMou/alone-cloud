package com.alone.coder.framework.file.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;
import com.alone.coder.framework.file.core.module.param.S3Param;
import com.alone.coder.framework.file.core.service.base.AbstractS3BaseFileService;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaojun
 */
@Slf4j
public class S3ServiceImpl extends AbstractS3BaseFileService<S3Param> {

    public S3ServiceImpl(String configId, S3Param param) {
        super(configId, param);
    }

    @Override
    public void init() {
        boolean isPathStyle = "path-style".equals(param.getPathStyle());
        String region = param.getRegion();
        if (StrUtil.isEmpty(param.getRegion()) && StrUtil.isNotEmpty(param.getEndPoint())) {
            region = param.getEndPoint().split("\\.")[1];
        }
        BasicAWSCredentials credentials = new BasicAWSCredentials(param.getAccessKey(), param.getSecretKey());
        s3Client = AmazonS3ClientBuilder.standard()
                .withPathStyleAccessEnabled(isPathStyle)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(param.getEndPoint(), region)).build();
    }

    @Override
    public StorageTypeEnum getStorageTypeEnum() {
        return StorageTypeEnum.S3;
    }

}