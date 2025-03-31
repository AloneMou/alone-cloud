package com.alone.coder.module.system.convert.oauth2;

import com.alone.coder.module.system.api.oauth2.vo.SystemOauthClientRespDTO;
import com.alone.coder.module.system.dal.dataobject.oauth2.SystemOauthClientDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SystemOauthClientConvert {

    SystemOauthClientConvert INSTANCE = Mappers.getMapper(SystemOauthClientConvert.class);


    SystemOauthClientRespDTO convert(SystemOauthClientDO bean);
}
