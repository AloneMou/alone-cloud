package com.alone.coder.framework.gray.core.chooser;

import com.alibaba.nacos.common.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RoundRuleChooser implements IRuleChooser{

    private final AtomicInteger position;

    public RoundRuleChooser() {
        this.position = new AtomicInteger(1000);
    }

    @Override
    public ServiceInstance choose(List<ServiceInstance> instances) {
        if(CollectionUtils.isNotEmpty(instances)){
            ServiceInstance serviceInstance = instances.get(Math.abs(position.incrementAndGet() % instances.size()));
            log.info("选择了ip为{}, 端口为：{}的服务", serviceInstance.getHost(), serviceInstance.getPort());
            return serviceInstance;
        }
        return null;
    }
}
