package com.alone.coder.module.system.dal.mysql.route;

import com.alone.coder.framework.common.enums.CommonStatusEnum;
import com.alone.coder.framework.mybatis.core.mapper.BaseMapperX;
import com.alone.coder.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.alone.coder.module.system.dal.dataobject.route.SystemRouteDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SystemRouteMapper extends BaseMapperX<SystemRouteDO> {

    /**
     * 获得可用的路由
     *
     * @return 路由列表
     */
    default List<SystemRouteDO> selectRoutesAvailable() {
        return selectList(new LambdaQueryWrapperX<SystemRouteDO>()
                .eq(SystemRouteDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .orderByAsc(SystemRouteDO::getSort)
        );
    }
}
