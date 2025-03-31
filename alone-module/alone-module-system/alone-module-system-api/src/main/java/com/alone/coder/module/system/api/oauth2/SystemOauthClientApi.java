package com.alone.coder.module.system.api.oauth2;

import com.alone.coder.framework.common.pojo.CommonResult;
import com.alone.coder.framework.feign.core.annotation.NoToken;
import com.alone.coder.module.system.api.oauth2.vo.SystemOauthClientRespDTO;
import com.alone.coder.module.system.enums.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "systemOauthClientApi", name = ApiConstants.SERVE_NAME)
public interface SystemOauthClientApi {

    @NoToken
    @GetMapping("/system/oauth2/client/get/{clientId}")
    CommonResult<SystemOauthClientRespDTO> getClient(@PathVariable("clientId") String clientId);
}
