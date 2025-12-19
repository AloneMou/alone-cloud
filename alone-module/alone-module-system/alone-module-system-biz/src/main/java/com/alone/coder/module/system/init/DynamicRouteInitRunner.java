
package com.alone.coder.module.system.init;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alone.coder.framework.common.util.json.JsonUtils;
import com.alone.coder.module.system.dal.dataobject.route.SystemRouteDO;
import com.alone.coder.module.system.init.vo.RouteDefinitionVO;
import com.alone.coder.module.system.service.route.SystemRouteService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scheduling.annotation.Async;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 容器启动后保存配置文件里面的路由信息到Redis
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnDiscoveryEnabled
public class DynamicRouteInitRunner implements InitializingBean {

    private final RedisTemplate<String, String> redisTemplate;

    private final SystemRouteService systemRouteService;

    private final RedisMessageListenerContainer listenerContainer;


    /**
     * 全局缓存，在缓存名称上加上该前缀表示该缓存不区分租户，比如:
     * <p/>
     * {@code @Cacheable(value = CacheConstants.GLOBALLY+CacheConstants.MENU_DETAILS, key = "#roleId  + '_menu'", unless = "#result == null")}
     */
    String GLOBALLY = "gl:";
    /**
     * 路由存放
     */
    String ROUTE_KEY = GLOBALLY + "gateway_route_key";

    /**
     * 内存reload 时间
     */
    String ROUTE_JVM_RELOAD_TOPIC = "gateway_jvm_route_reload_topic";

    /**
     * redis 重新加载 路由信息
     */
    String ROUTE_REDIS_RELOAD_TOPIC = "upms_redis_route_reload_topic";


    /**
     * WebServerInitializedEvent 使用 TransactionalEventListener 时启动时无法获取到事件
     */
    @Async
    @Order
    @EventListener({WebServerInitializedEvent.class})
    public void WebServerInit() {
        this.initRoute();
    }

    @Async
    @Order
    public void initRoute() {
        redisTemplate.delete(ROUTE_KEY);
        log.info("开始初始化网关路由");
        List<SystemRouteDO> routeLs = systemRouteService.getRouteAvailableList();
        for (SystemRouteDO route : routeLs) {
            RouteDefinitionVO vo = new RouteDefinitionVO();
            vo.setRouteName(route.getName());
            vo.setId(route.getId().toString());
            vo.setUri(URI.create(route.getUri()));
            vo.setOrder(route.getSort());

            vo.setFilters(route.getFilters());
            JSONArray predicateObj = JSONUtil.parseArray(route.getPredicates());
            vo.setPredicates(predicateObj.toList(SystemRouteDO.PredicateDefinition.class));
            vo.setMetadata(JsonUtils.parseObject(route.getMetadata(), new TypeReference<>() {
            }));
            log.info("加载路由ID：{},{}", route.getId(), vo);
            redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(RouteDefinitionVO.class));
            HashOperations<String, String, RouteDefinitionVO> stringStringValueOperations = redisTemplate.opsForHash();
            stringStringValueOperations.put(ROUTE_KEY, route.getId().toString(), vo);
        }
        // 通知网关重置路由
        redisTemplate.convertAndSend(ROUTE_JVM_RELOAD_TOPIC, "");
        log.debug("初始化网关路由结束 ");
    }

    /**
     * redis 监听配置,监听 upms_redis_route_reload_topic,重新加载Redis
     */
    @Override
    public void afterPropertiesSet() {
        listenerContainer.addMessageListener((message, bytes) -> {
            log.warn("接收到重新Redis 重新加载路由事件");
            initRoute();
        }, new ChannelTopic(ROUTE_REDIS_RELOAD_TOPIC));
    }

}
