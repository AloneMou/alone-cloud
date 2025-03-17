package com.alone.coder.framework.gray.core.chooser;

import cn.hutool.core.util.IdUtil;
import com.alibaba.nacos.client.naming.utils.Chooser;
import com.alibaba.nacos.client.naming.utils.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RandomRuleChooser implements IRuleChooser {

    @Override
    public ServiceInstance choose(List<ServiceInstance> instances) {
        if (instances.size() == 1) {
            return instances.get(0);
        } else {
            List<Pair<ServiceInstance>> hostsWithWeight = new ArrayList<>();
            for (ServiceInstance serviceInstance : instances) {
                if ("true".equals(serviceInstance.getMetadata().getOrDefault("nacos.healthy", "true"))) {
                    hostsWithWeight.add(new Pair<>(serviceInstance,
                            Double.parseDouble(serviceInstance.getMetadata().getOrDefault("nacos.weight", "1"))));
                }
            }
            Chooser<String, ServiceInstance> vipChooser = new Chooser<>(IdUtil.nanoId(), hostsWithWeight);
            return vipChooser.randomWithWeight();
        }
    }
}
