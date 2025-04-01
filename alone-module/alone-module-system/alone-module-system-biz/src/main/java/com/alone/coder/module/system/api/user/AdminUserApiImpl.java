package com.alone.coder.module.system.api.user;

import com.alone.coder.framework.common.pojo.CommonResult;
import com.alone.coder.framework.security.core.annotation.Inner;
import com.alone.coder.module.system.api.user.vo.UserInfo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class AdminUserApiImpl implements AdminUserApi {

    @Override
    @Inner(value = false)
    public CommonResult<UserInfo> info(@PathVariable("username") String username) {
        return CommonResult.success(null);
    }
}
