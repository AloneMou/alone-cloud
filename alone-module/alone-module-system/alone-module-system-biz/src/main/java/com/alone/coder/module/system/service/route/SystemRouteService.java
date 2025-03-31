package com.alone.coder.module.system.service.route;

import com.alone.coder.module.system.controller.route.vo.SystemRouteCreateReqVO;
import com.alone.coder.module.system.controller.route.vo.SystemRouteUpdateReqVO;
import com.alone.coder.module.system.dal.dataobject.route.SystemRouteDO;

import java.util.List;

public interface SystemRouteService {

    /**
     * 创建路由
     *
     * @param createReqVO 创建信息
     */
    void createRoute(SystemRouteCreateReqVO createReqVO);

    /**
     * 更新路由
     *
     * @param updateReqVO 更新信息
     */
    void updateRoute(SystemRouteUpdateReqVO updateReqVO);

    /**
     * 查询可用的路由列表
     *
     * @return 路由列表
     */
    List<SystemRouteDO> getRouteAvailableList();
}
