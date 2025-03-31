package com.alone.coder.module.system.controller.route.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 路由创建 Request VO")
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemRouteCreateReqVO extends SystemRouteBaseVO {
}
