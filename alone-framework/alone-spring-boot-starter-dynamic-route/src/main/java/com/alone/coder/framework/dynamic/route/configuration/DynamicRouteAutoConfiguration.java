
package com.alone.coder.framework.dynamic.route.configuration;

import com.alone.coder.framework.dynamic.route.core.message.RouteConsumer;
import com.alone.coder.framework.dynamic.route.support.DynamicRouteHealthIndicator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author lengleng
 * @date 2018/11/5
 * <p>
 * 动态路由配置类
 */
@Slf4j
@Configuration
@ComponentScan("com.alone.coder.framework.dynamic.route")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class DynamicRouteAutoConfiguration {

    /**
     * 内存reload 时间
     */
    String ROUTE_JVM_RELOAD_TOPIC = "gateway_jvm_route_reload_topic";

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
     * redis 监听配置
     */
    @Bean
    public RouteConsumer routeConsumer() {
        return new RouteConsumer();
    }


    /**
     * 动态路由监控检查
     *
     * @param redisTemplate redis
     * @return
     */
    @Bean
    public DynamicRouteHealthIndicator healthIndicator(RedisTemplate redisTemplate) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        if (!redisTemplate.hasKey(ROUTE_KEY)) {
            log.info("路由信息未初始化，网关路由失败");
        }
        return new DynamicRouteHealthIndicator(redisTemplate);
    }

}
