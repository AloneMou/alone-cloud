package com.alone.coder.framework.gray.core.rule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.NacosServiceInstance;
import com.alibaba.nacos.client.naming.utils.Chooser;
import com.alibaba.nacos.client.naming.utils.Pair;
import com.alone.coder.framework.gray.core.chooser.IRuleChooser;
import com.alone.coder.framework.gray.core.constants.FeignCommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2020/11/20
 */
@Slf4j
public class GrayRoundRobinLoadBalancer extends RoundRobinLoadBalancer {


    private final static String KEY_DEFAULT = "default";

    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    private String serviceId;

    private IRuleChooser ruleChooser;

    /**
     * @param serviceInstanceListSupplierProvider a provider of
     *                                            {@link ServiceInstanceListSupplier} that will be used to get available instances
     * @param serviceId                           id of the service for which to choose an instance
     */
    public GrayRoundRobinLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                                      String serviceId, IRuleChooser ruleChooser) {
        super(serviceInstanceListSupplierProvider, serviceId);
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.serviceId = serviceId;
        this.ruleChooser = ruleChooser;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(serviceInstances -> getInstanceResponse(serviceInstances, request));

    }

    Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, Request request) {
        // 注册中心无可用实例 抛出异常
        if (CollUtil.isEmpty(instances)) {
            log.warn("No instance available serviceId: {}", serviceId);
            return new EmptyResponse();
        }

        if (request == null || request.getContext() == null) {
            return super.choose(request).block();
        }

        DefaultRequestContext requestContext = (DefaultRequestContext) request.getContext();
        if (!(requestContext.getClientRequest() instanceof RequestData clientRequest)) {
            return super.choose(request).block();
        }
        HttpHeaders headers = clientRequest.getHeaders();

        String reqVersion = headers.getFirst(FeignCommonConstants.VERSION);
        if (StrUtil.isBlank(reqVersion)) {
            // 过滤出不含VERSION实例
            List<ServiceInstance> versionInstanceList = instances.stream()
                    .filter(instance -> !instance.getMetadata().containsKey(FeignCommonConstants.VERSION))
                    .collect(Collectors.toList());
            if (CollUtil.isEmpty(versionInstanceList)) {
                // 根据权重获取实例
                return new DefaultResponse(ruleChooser.choose(instances));
            }
            // 根据权重获取实例
            return new DefaultResponse(ruleChooser.choose(versionInstanceList));
        }

        // 遍历可以实例元数据，若匹配则返回此实例
        List<ServiceInstance> serviceInstanceList = instances.stream().filter(instance -> {
            NacosServiceInstance nacosInstance = (NacosServiceInstance) instance;
            Map<String, String> metadata = nacosInstance.getMetadata();
            String targetVersion = MapUtil.getStr(metadata, FeignCommonConstants.VERSION);
            return reqVersion.equalsIgnoreCase(targetVersion);
        }).collect(Collectors.toList());
        // 如果没有匹配的，返回默认实例
        if (CollUtil.isNotEmpty(serviceInstanceList)) {
            ServiceInstance instance = ruleChooser.choose(serviceInstanceList);
            log.debug("gray instance available serviceId: {} , instanceId: {}", serviceId, instance.getInstanceId());
            return new DefaultResponse(instance);
        } else {
            // 不存在,降级策略，使用轮询策略
            return super.choose(request).block();
        }
    }


}
