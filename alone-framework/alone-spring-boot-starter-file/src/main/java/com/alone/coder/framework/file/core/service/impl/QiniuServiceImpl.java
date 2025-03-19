package com.alone.coder.framework.file.core.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.alone.coder.framework.common.util.string.StrUtils;
import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;
import com.alone.coder.framework.file.core.module.param.QiniuParam;
import com.alone.coder.framework.file.core.service.base.AbstractS3BaseFileService;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.util.Auth;

/**
 * 七牛云 KODO 存储服务实现类
 *
 * @author AgoniMou
 */
public class QiniuServiceImpl extends AbstractS3BaseFileService<QiniuParam> {

    private BucketManager bucketManager;
    private Auth auth;

    public QiniuServiceImpl(String configId, QiniuParam param) {
        super(configId, param);
    }

    @Override
    public void init() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(param.getAccessKey(), param.getSecretKey());
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(param.getEndPoint(), "kodo"))
                .build();

        Configuration cfg = new Configuration(Region.autoRegion());
        auth = Auth.create(param.getAccessKey(), param.getSecretKey());
        bucketManager = new BucketManager(auth, cfg);
    }

    @Override
    public StorageTypeEnum getStorageTypeEnum() {
        return StorageTypeEnum.QINIU;
    }

    @Override
    public String getDownloadUrl(String pathAndName) {
        String domain = param.getDomain();
        Integer tokenTime = param.getTokenTime();
        if (param.getTokenTime() == null || param.getTokenTime() < 1) {
            tokenTime = 1800;
        }
        String fullPath = StrUtils.concatTrimStartSlashes(param.getBasePath() + pathAndName);
        // 如果不是私有空间, 且指定了加速域名, 则使用 qiniu 的 sdk 获取下载链接
        // (使用 s3 sdk 获取到的下载链接替换自动加速域名后无法访问, 故这里使用 qiniu sdk).
        if (BooleanUtil.isTrue(param.isPrivate()) && StrUtil.isNotEmpty(domain)) {
            String customDomainFullPath = StrUtils.removeDuplicateSlashes(domain + "/" + StrUtils.encodeAllIgnoreSlashes(fullPath));
            return auth.privateDownloadUrl(customDomainFullPath, tokenTime);
        }
        return super.getDownloadUrl(pathAndName);
    }
}
