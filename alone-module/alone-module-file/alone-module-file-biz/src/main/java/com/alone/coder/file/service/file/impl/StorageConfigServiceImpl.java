package com.alone.coder.file.service.file.impl;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.IdUtil;

import com.alone.coder.file.controller.admin.file.vo.config.StorageConfigCreateReqVO;
import com.alone.coder.file.controller.admin.file.vo.config.StorageConfigPageReqVO;
import com.alone.coder.file.controller.admin.file.vo.config.StorageConfigUpdateReqVO;
import com.alone.coder.file.convert.file.StorageConfigConvert;
import com.alone.coder.file.dal.dataobject.file.StorageConfig;
import com.alone.coder.file.dal.mysql.file.StorageConfigMapper;
import com.alone.coder.file.mq.producer.file.StorageConfigProducer;
import com.alone.coder.file.service.file.StorageConfigService;
import com.alone.coder.framework.common.pojo.PageResult;
import com.alone.coder.framework.common.util.json.JsonUtils;
import com.alone.coder.framework.common.util.validation.ValidationUtils;
import com.alone.coder.framework.file.core.context.StorageSourceContext;
import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;
import com.alone.coder.framework.file.core.module.param.IStorageParam;
import com.alone.coder.framework.file.core.service.base.BaseFileService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.validation.Validator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.validation.annotation.Validated;


import java.util.List;
import java.util.Map;

import static com.alone.coder.file.enums.ErrorCodeConstants.STORAGE_CONFIG_DELETE_FAIL_MASTER;
import static com.alone.coder.file.enums.ErrorCodeConstants.STORAGE_CONFIG_NOT_EXISTS;
import static com.alone.coder.framework.common.exception.util.ServiceExceptionUtil.exception;


/**
 * @author AgoniMou
 */
@Slf4j
@Validated
@Service
public class StorageConfigServiceImpl implements StorageConfigService {

    @Resource
    private StorageConfigMapper storageConfigMapper;
    @Resource
    private StorageSourceContext storageSourceContext;

    @Resource
    private StorageConfigProducer storageConfigProducer;

    /**
     * Master FileClient 对象，有且仅有一个，即 {@link StorageConfig#getMaster()} 对应的
     */
    @Getter
    private BaseFileService masterFileClient;

    @Resource
    private Validator validator;

    @Override
    @PostConstruct
    public void initLocalCache() {
        // 第一步：查询数据
        List<StorageConfig> configs = storageConfigMapper.selectList();
        log.info("[initLocalCache][缓存文件配置，数量为:{}]", configs.size());
        // 第二步：构建缓存：创建或更新文件 Client
        configs.forEach(config -> {
            storageSourceContext.createOrUpdateStorageClient(config.getId(), config.getStorage(), config.getConfig());
            // 如果是 master，进行设置
            if (Boolean.TRUE.equals(config.getMaster())) {
                masterFileClient = storageSourceContext.getByStorageId(config.getId());
            }
        });
    }

    @Override
    public PageResult<StorageConfig> getStorageConfigPage(StorageConfigPageReqVO reqVO) {
        return storageConfigMapper.getStorageConfigPage(reqVO);
    }

    @Override
    public String createFileConfig(StorageConfigCreateReqVO createReqVO) {
        // 插入
        StorageConfig storageConfig = StorageConfigConvert.INSTANCE.convert(createReqVO).setConfig(parseClientConfig(createReqVO.getStorage(), createReqVO.getConfig())).setMaster(false); // 默认非 master
        storageConfigMapper.insert(storageConfig);
        // 发送刷新配置的消息
        storageConfigProducer.sendFileConfigRefreshMessage();
        // 返回
        return storageConfig.getId();
    }

    @Override
    public void updateFileConfig(StorageConfigUpdateReqVO updateReqVO) {
        // 校验存在
        StorageConfig config = validateStorageConfigExists(updateReqVO.getId());
        // 更新
        StorageConfig updateObj = StorageConfigConvert.INSTANCE.convert(updateReqVO).setConfig(parseClientConfig(config.getStorage(), updateReqVO.getConfig()));
        storageConfigMapper.updateById(updateObj);
        // 发送刷新配置的消息
        storageConfigProducer.sendFileConfigRefreshMessage();
    }

    @Override
    public void updateFileConfigMaster(String id) {
        // 校验存在
        validateStorageConfigExists(id);
        // 更新其它为非 master
        storageConfigMapper.updateBatch(new StorageConfig().setMaster(false));
        // 更新
        storageConfigMapper.updateById(new StorageConfig().setId(id).setMaster(true));
        // 发送刷新配置的消息
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                storageConfigProducer.sendFileConfigRefreshMessage();
            }
        });
    }

    @Override
    public void deleteFileConfig(String id) {
        // 校验存在
        StorageConfig config = validateStorageConfigExists(id);
        if (Boolean.TRUE.equals(config.getMaster())) {
            throw exception(STORAGE_CONFIG_DELETE_FAIL_MASTER);
        }
        // 删除
        storageConfigMapper.deleteById(id);
        // 发送刷新配置的消息
        storageConfigProducer.sendFileConfigRefreshMessage();
    }

    @Override
    public StorageConfig getStorageConfig(String id) {
        return storageConfigMapper.selectById(id);
    }

    @Override
    public String testFileConfig(String id) throws Exception {
        // 校验存在
        validateStorageConfigExists(id);
        // 上传文件
        byte[] content = ResourceUtil.readBytes("file/test.jpg");
        return storageSourceContext.getByStorageId(id).upload(content, IdUtil.fastSimpleUUID() + ".jpg", "image/jpeg");
    }

    @Override
    public BaseFileService getFileClient(String id) {
        return storageSourceContext.getByStorageId(id);
    }

    private StorageConfig validateStorageConfigExists(String id) {
        StorageConfig config = storageConfigMapper.selectById(id);
        if (config == null) {
            throw exception(STORAGE_CONFIG_NOT_EXISTS);
        }
        return config;
    }


    private IStorageParam parseClientConfig(String storage, Map<String, Object> config) {
        // 获取配置类
        Class<? extends IStorageParam> configClass = StorageTypeEnum.getByStorage(storage).getConfigClass();
        IStorageParam clientConfig = JsonUtils.parseObject2(JsonUtils.toJsonString(config), configClass);
        // 参数校验
        ValidationUtils.validate(validator, clientConfig);
        // 设置参数
        return clientConfig;
    }
}
