package com.alone.coder.framework.dynamic.route.core.message;

import com.alone.coder.framework.common.util.spring.SpringUtils;
import com.alone.coder.framework.dynamic.route.core.message.vo.RouteMessage;
import com.alone.coder.framework.dynamic.route.support.RouteCacheHolder;
import com.alone.coder.framework.redis.core.pubsub.AbstractRedisChannelMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;

@Slf4j
public class RouteConsumer extends AbstractRedisChannelMessageListener<RouteMessage> {

    /**
     * 内存reload 时间
     */
    private final static String ROUTE_JVM_RELOAD_TOPIC = "gateway_jvm_route_reload_topic";

    public RouteConsumer() {
        super(ROUTE_JVM_RELOAD_TOPIC);
    }

    @Override
    public void onMessage(RouteMessage message) {
        log.warn("接收到重新JVM 重新加载路由事件");
        RouteCacheHolder.removeRouteList();
        // 发送刷新路由事件
        SpringUtils.publishEvent(new RefreshRoutesEvent(this));
    }

}
