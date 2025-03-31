package com.alone.coder.module.system.service.oauth2;

import com.alone.coder.module.system.dal.dataobject.oauth2.SystemOauthClientDO;
import com.alone.coder.module.system.dal.mysql.oauth2.SystemOauthClientMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SystemOauthClientServiceImpl implements SystemOauthClientService {

    @Resource
    private SystemOauthClientMapper oauthClientMapper;


    @Override
    public SystemOauthClientDO getByClientId(String clientId) {
        return oauthClientMapper.selectByClientId(clientId);
    }
}
