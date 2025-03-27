package com.alone.coder.module.system.api.user;

import com.alone.coder.framework.common.pojo.CommonResult;
import com.alone.coder.module.system.api.user.vo.UserInfo;
import com.alone.coder.module.system.enums.ApiConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "adminUserApi", name = ApiConstants.SERVE_NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "Feign 服务 - 管理员用户")
public interface AdminUserApi {

    @GetMapping("/system/user/info/{username}")
    CommonResult<UserInfo> info(@PathVariable("username") String username);
}
