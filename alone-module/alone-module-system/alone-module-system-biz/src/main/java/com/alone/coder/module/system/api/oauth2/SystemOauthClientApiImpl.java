package com.alone.coder.module.system.api.oauth2;

import com.alone.coder.framework.common.pojo.CommonResult;
import com.alone.coder.framework.security.core.annotation.Inner;
import com.alone.coder.module.system.api.oauth2.vo.SystemOauthClientRespDTO;
import com.alone.coder.module.system.convert.oauth2.SystemOauthClientConvert;
import com.alone.coder.module.system.dal.dataobject.oauth2.SystemOauthClientDO;
import com.alone.coder.module.system.service.oauth2.SystemOauthClientService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
@RequestMapping
public class SystemOauthClientApiImpl implements SystemOauthClientApi {


    @Resource
    private SystemOauthClientService systemOauthClientService;

    @Override
    @Inner(value = false)
    public CommonResult<SystemOauthClientRespDTO> getClient(String clientId) {
        SystemOauthClientDO clientDO = systemOauthClientService.getByClientId(clientId);
        return CommonResult.success(SystemOauthClientConvert.INSTANCE.convert(clientDO));
    }
}
