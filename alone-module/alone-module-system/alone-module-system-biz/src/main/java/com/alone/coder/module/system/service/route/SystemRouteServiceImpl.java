package com.alone.coder.module.system.service.route;

import com.alone.coder.module.system.controller.route.vo.SystemRouteCreateReqVO;
import com.alone.coder.module.system.controller.route.vo.SystemRouteUpdateReqVO;
import com.alone.coder.module.system.dal.dataobject.route.SystemRouteDO;
import com.alone.coder.module.system.dal.mysql.route.SystemRouteMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SystemRouteServiceImpl implements SystemRouteService {

    @Resource
    private SystemRouteMapper systemRouteMapper;


    @Override
    public void createRoute(SystemRouteCreateReqVO createReqVO) {

    }

    @Override
    public void updateRoute(SystemRouteUpdateReqVO updateReqVO) {

    }

    @Override
    public List<SystemRouteDO> getRouteAvailableList() {
        return systemRouteMapper.selectRoutesAvailable();
    }
}
