package com.alone.coder.module.system.controller.route.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 路由更新 Request VO")
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemRouteUpdateReqVO extends SystemRouteBaseVO {

    @NotNull(message = "路由ID不能为空")
    @Schema(description = "路由ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;
}
