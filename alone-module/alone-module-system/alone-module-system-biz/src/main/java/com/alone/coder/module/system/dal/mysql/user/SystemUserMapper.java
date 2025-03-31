package com.alone.coder.module.system.dal.mysql.user;

import com.alone.coder.framework.mybatis.core.mapper.BaseMapperX;
import com.alone.coder.module.system.dal.dataobject.user.SystemUserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SystemUserMapper extends BaseMapperX<SystemUserDO> {

    default SystemUserDO selectByUsername(String username) {
        return selectOne(SystemUserDO::getUsername, username);
    }

    default SystemUserDO selectByMobile(String mobile) {
        return selectOne(SystemUserDO::getMobile, mobile);
    }

    default SystemUserDO selectByEmail(String email) {
        return selectOne(SystemUserDO::getEmail, email);
    }
}
