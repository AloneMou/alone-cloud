/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.alone.coder.framework.dynamic.route.support;

import cn.hutool.core.collection.CollUtil;
import com.alone.coder.framework.dynamic.route.vo.RouteDefinitionVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author lengleng
 * @date 2018/10/31
 * <p>
 * redis 保存路由信息，优先级比配置文件高
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisRouteDefinitionWriter implements RouteDefinitionRepository {

	private final RedisTemplate<String, String> redisTemplate;


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

	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		return route.flatMap(r -> {
			RouteDefinitionVo vo = new RouteDefinitionVo();
			BeanUtils.copyProperties(r, vo);
			log.info("保存路由信息{}", vo);
			redisTemplate.opsForHash().put(ROUTE_KEY, r.getId(), vo);
			redisTemplate.convertAndSend(ROUTE_JVM_RELOAD_TOPIC, "新增路由信息,网关缓存更新");
			return Mono.empty();
		});
	}

	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		routeId.subscribe(id -> {
			log.info("删除路由信息{}", id);
			redisTemplate.opsForHash().delete(ROUTE_KEY, id);
		});
		redisTemplate.convertAndSend(ROUTE_JVM_RELOAD_TOPIC, "删除路由信息,网关缓存更新");
		return Mono.empty();
	}

	/**
	 * 动态路由入口
	 * <p>
	 * 1. 先从内存中获取 2. 为空加载Redis中数据 3. 更新内存
	 * @return
	 */
	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		List<RouteDefinitionVo> routeList = RouteCacheHolder.getRouteList();
		if (CollUtil.isNotEmpty(routeList)) {
			log.debug("内存 中路由定义条数： {}， {}", routeList.size(), routeList);
			return Flux.fromIterable(routeList);
		}

		redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(RouteDefinitionVo.class));
		HashOperations<String, String, RouteDefinitionVo> stringObjectObjectHashOperations = redisTemplate.opsForHash();
		List<RouteDefinitionVo> values = stringObjectObjectHashOperations.values(ROUTE_KEY);
		log.info("redis 中路由定义条数： {}， {}", values.size(), values);

		RouteCacheHolder.setRouteList(values);
		return Flux.fromIterable(values);
	}

}
