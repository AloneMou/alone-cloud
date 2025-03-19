package com.alone.coder.framework.file.core.context;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import com.alone.coder.framework.file.core.error.exception.InvalidStorageSourceException;
import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;
import com.alone.coder.framework.file.core.module.param.IStorageParam;
import com.alone.coder.framework.file.core.service.base.AbstractBaseFileService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author AgoniMou
 */
@Slf4j
public class StorageSourceContext {

    /**
     * 文件客户端 Map
     * key：配置编号
     */
    private final ConcurrentMap<String, AbstractBaseFileService<?>> clients = new ConcurrentHashMap<>();


    /**
     * 根据存储源 id 获取对应的 Service.
     *
     * @param configId 存储源 ID
     * @return 存储源对应的 Service
     */
    public AbstractBaseFileService<?> getByStorageId(String configId) {
        AbstractBaseFileService<?> abstractBaseFileService = clients.get(configId);
        if (abstractBaseFileService == null) {
            log.error("[getByStorageId][配置编号({}) 找不到客户端]", configId);
            throw new InvalidStorageSourceException();
        }
        return abstractBaseFileService;
    }


    /**
     * 创建或更新存储客户端
     *
     * @param configId 配置号
     * @param storage  存储
     * @param param    存储参数
     */
    @SuppressWarnings("unchecked")
    public <P extends IStorageParam> void createOrUpdateStorageClient(String configId, String storage, P param) {
        AbstractBaseFileService<P> client = (AbstractBaseFileService<P>) clients.get(configId);
        if (client == null) {
            client = this.createStorageClient(configId, storage, param);
            client.init();
            clients.put(client.getStorageId(), client);
        } else {
            client.refresh(param);
        }
    }


    /**
     * 创建存储客户端
     *
     * @param configId 配置号
     * @param storage  存储
     * @param param    存储参数
     * @return {@link AbstractBaseFileService }<{@link P }>
     */
    @SuppressWarnings("unchecked")
    private <P extends IStorageParam> AbstractBaseFileService<P> createStorageClient(String configId, String storage, P param) {
        StorageTypeEnum storageEnum = StorageTypeEnum.getByStorage(storage);
        Assert.notNull(storageEnum, String.format("文件配置(%s) 为空", storageEnum));
        // 创建客户端
        return (AbstractBaseFileService<P>) ReflectUtil.newInstance(storageEnum.getClientClass(), configId, param);
    }
}
