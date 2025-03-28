package com.alone.coder.framework.dynamic.route.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author lengleng
 * @date 2020/11/19
 * <p>
 * 动态路由检查检查
 */
@Slf4j
@RequiredArgsConstructor
public class DynamicRouteHealthIndicator extends AbstractHealthIndicator {

	private final RedisTemplate redisTemplate;

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

	@Override
	protected void doHealthCheck(Health.Builder builder) {
		if (Boolean.TRUE.equals(redisTemplate.hasKey(ROUTE_KEY))) {
			builder.up();
		}
		else {
			log.warn("动态路由监控检查失败，当前无路由配置");
			builder.down();
		}
	}

}
