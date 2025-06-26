package com.alone.coder.file.mq.consumer.file;


import com.alone.coder.file.mq.message.file.StorageConfigRefreshMessage;
import com.alone.coder.file.service.file.StorageConfigService;
import com.alone.coder.framework.redis.core.pubsub.AbstractRedisChannelMessageListener;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 针对 {@link StorageConfigRefreshMessage} 的消费者
 *
 * @author AgoniMou
 */
@Slf4j
@Component
public class StorageConfigRefreshConsumer extends AbstractRedisChannelMessageListener<StorageConfigRefreshMessage> {
    @Resource
    private StorageConfigService storageConfigService;

    @Override
    public void onMessage(StorageConfigRefreshMessage message) {
        log.info("[onMessage][收到 FileConfig 刷新消息]");
        storageConfigService.initLocalCache();
    }
}
