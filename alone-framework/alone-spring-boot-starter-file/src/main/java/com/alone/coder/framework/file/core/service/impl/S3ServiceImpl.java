package com.alone.coder.framework.file.core.service.impl;

import cn.hutool.http.HttpUtil;
import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;
import com.alone.coder.framework.file.core.module.param.S3Param;
import com.alone.coder.framework.file.core.service.base.AbstractS3BaseFileService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

import static com.alone.coder.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.alone.coder.framework.file.core.error.code.StorageErrorCode.STORAGE_SOURCE_S3_DOMAIN_STYLE_ERROR;

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
        String endPoint = param.getEndPoint();
        String endPointScheme = param.getEndPointScheme();
        // 如果 endPoint 不包含协议部分, 且配置了 endPointScheme, 则手动拼接协议部分.
        if (!HttpUtil.isHttp(endPoint) && StringUtils.isNotBlank(endPointScheme)) {
            endPoint = endPointScheme + "://" + endPoint;
        }

        boolean isPathStyle = "path-style".equals(param.getPathStyle());
        String domain = param.getDomain();
        if (StringUtils.isNotBlank(domain) && !isPathStyle) {
            throw exception(STORAGE_SOURCE_S3_DOMAIN_STYLE_ERROR);
        }

        String region = param.getRegion();
        if (StringUtils.isEmpty(param.getRegion()) && StringUtils.isNotEmpty(endPoint)) {
            region = endPoint.split("\\.")[1];
        }

        Region oss = Region.of(region);
        URI endpointOverride = URI.create(endPoint);
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create(param.getAccessKey(), param.getSecretKey()));

        super.s3Client = S3Client.builder()
                .overrideConfiguration(getClientConfiguration())
                .forcePathStyle(isPathStyle)
                .region(oss)
                .endpointOverride(endpointOverride)
                .credentialsProvider(credentialsProvider)
                .build();

        super.s3PresignerDownload = S3Presigner.builder()
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(isPathStyle)
                        .build())
                .region(oss)
                .endpointOverride(StringUtils.isBlank(domain) ? endpointOverride : URI.create(domain))
                .credentialsProvider(credentialsProvider)
                .build();

        super.s3Presigner = S3Presigner.builder()
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(isPathStyle)
                        .build())
                .region(oss)
                .endpointOverride(endpointOverride)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Override
    public StorageTypeEnum getStorageTypeEnum() {
        return StorageTypeEnum.S3;
    }

}