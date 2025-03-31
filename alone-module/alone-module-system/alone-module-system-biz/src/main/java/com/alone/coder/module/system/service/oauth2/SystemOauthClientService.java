package com.alone.coder.module.system.service.oauth2;

import com.alone.coder.module.system.dal.dataobject.oauth2.SystemOauthClientDO;
import com.alone.coder.module.system.dal.mysql.oauth2.SystemOauthClientMapper;

public interface SystemOauthClientService {

    /**
     * 根据 clientId 获取客户端信息
     *
     * @param clientId 客户端编号
     * @return 客户端信息
     */
    SystemOauthClientDO getByClientId(String clientId);
}
