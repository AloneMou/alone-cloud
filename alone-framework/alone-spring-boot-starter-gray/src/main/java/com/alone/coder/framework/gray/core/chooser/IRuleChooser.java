package com.alone.coder.framework.gray.core.chooser;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public interface IRuleChooser {

	ServiceInstance choose(List<ServiceInstance> instances);

}
