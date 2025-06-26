package com.alone.coder.file.mq.producer.file;


import com.alone.coder.file.mq.message.file.StorageConfigRefreshMessage;
import com.alone.coder.framework.redis.core.RedisMQTemplate;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;


/**
 * @author AgoniMou
 */
@Component
public class StorageConfigProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    /**
     * 发送 {@link StorageConfigRefreshMessage} 消息
     */
    public void sendFileConfigRefreshMessage() {
        StorageConfigRefreshMessage message = new StorageConfigRefreshMessage();
        redisMQTemplate.send(message);
    }

}
