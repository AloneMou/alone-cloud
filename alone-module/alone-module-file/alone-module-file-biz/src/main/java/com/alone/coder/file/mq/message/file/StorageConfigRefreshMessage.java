package com.alone.coder.file.mq.message.file;

import com.alone.coder.framework.common.util.redis.message.AbstractRedisChannelMessage;
import org.springframework.stereotype.Component;

/**
 * @author AgoniMou
 */
@Component
public class StorageConfigRefreshMessage extends AbstractRedisChannelMessage {

    @Override
    public String getChannel() {
        return "infra.storage-config.refresh";
    }
}
