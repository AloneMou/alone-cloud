package com.alone.coder.module.system.dal.mysql.oauth2;

import com.alone.coder.framework.mybatis.core.mapper.BaseMapperX;
import com.alone.coder.module.system.dal.dataobject.oauth2.SystemOauthClientDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SystemOauthClientMapper extends BaseMapperX<SystemOauthClientDO> {

    default SystemOauthClientDO selectByClientId(String clientId) {
        return selectOne(SystemOauthClientDO::getClientId, clientId);
    }
}
