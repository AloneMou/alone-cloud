package com.alone.coder.module.system.api.user;

import com.alone.coder.framework.common.pojo.CommonResult;
import com.alone.coder.framework.feign.core.annotation.NoToken;
import com.alone.coder.module.system.api.user.vo.UserInfo;
import com.alone.coder.module.system.enums.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "adminUserApi", name = ApiConstants.SERVE_NAME)
public interface AdminUserApi {

    @NoToken
    @GetMapping("/system/user/info/{username}")
    CommonResult<UserInfo> info(@PathVariable("username") String username);
}
