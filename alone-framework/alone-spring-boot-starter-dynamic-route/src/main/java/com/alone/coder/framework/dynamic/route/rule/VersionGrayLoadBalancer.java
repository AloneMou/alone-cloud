package com.alone.coder.framework.dynamic.route.rule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.client.naming.utils.Chooser;
import com.alibaba.nacos.client.naming.utils.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2020/1/12
 * <p>
 * 基于客户端版本号灰度路由，增加基于nacos权重获取服务
 */
@Slf4j
@RequiredArgsConstructor
public class VersionGrayLoadBalancer implements GrayLoadBalancer {

	private final DiscoveryClient discoveryClient;


	/**
	 * header 中版本信息
	 */
	String VERSION = "VERSION";

	/**
	 * 根据serviceId 筛选可用服务
	 * @param serviceId 服务ID
	 * @param request 当前请求
	 * @return 服务实例：ServiceInstance
	 */
	@Override
	public ServiceInstance choose(String serviceId, ServerHttpRequest request) {
		List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
		// 注册中心无实例 抛出异常
		if (CollUtil.isEmpty(instances)) {
			log.warn("No instance available for {}", serviceId);
			throw new NotFoundException("No instance available for " + serviceId);
		}
		// 获取请求version，无则随机返回可用实例
		String reqVersion = request.getHeaders().getFirst(VERSION);
		if (StrUtil.isBlank(reqVersion)) {
			// 过滤出不含VERSION实例
			List<ServiceInstance> versionInstanceList = instances.stream()
				.filter(instance -> !instance.getMetadata().containsKey(VERSION))
				.collect(Collectors.toList());
			if (CollUtil.isEmpty(versionInstanceList)) {
				// 根据权重获取实例
				return randomByWeight(instances);
			}
			// 根据权重获取实例
			return randomByWeight(versionInstanceList);
		}
		// 遍历可以实例元数据，若匹配则返回此实例
		List<ServiceInstance> availableList = instances.stream()
			.filter(instance -> reqVersion
				.equalsIgnoreCase(MapUtil.getStr(instance.getMetadata(), VERSION)))
			.collect(Collectors.toList());
		if (CollUtil.isEmpty(availableList)) {
			// 根据权重获取实例
			return randomByWeight(instances);
		}
		// 根据权重获取实例
		return randomByWeight(availableList);
	}

	/**
	 * 根据nacos设置的权重返回实例
	 * @param serviceInstances 服务实例集合
	 * @return 服务实例：ServiceInstance
	 */
	protected ServiceInstance randomByWeight(final List<ServiceInstance> serviceInstances) {
		if (serviceInstances.size() == 1) {
			return serviceInstances.get(0);
		}
		else {
			List<Pair<ServiceInstance>> hostsWithWeight = new ArrayList<>();
			for (ServiceInstance serviceInstance : serviceInstances) {
				if ("true".equals(serviceInstance.getMetadata().getOrDefault("nacos.healthy", "true"))) {
					hostsWithWeight.add(new Pair<>(serviceInstance,
							Double.parseDouble(serviceInstance.getMetadata().getOrDefault("nacos.weight", "1"))));
				}
			}
			Chooser<String, ServiceInstance> vipChooser = new Chooser<>("www.taobao.com", hostsWithWeight);
			return vipChooser.randomWithWeight();
		}
	}

}
