package com.alone.coder.module.system.controller.route.vo;

import com.alone.coder.framework.common.enums.CommonStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 路由 Base VO，提供给添加、修改、详细的子 VO 使用")
@Data
public class SystemRouteBaseVO {


    /**
     * 路由名称
     */
    @NotBlank(message = "路由名称不能为空")
    @Schema(description = "路由名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
    private String name;

    /**
     * 路由路径
     */
    @NotBlank(message = "路由路径不能为空")
    @Schema(description = "路由路径", requiredMode = Schema.RequiredMode.REQUIRED, example = "lb://auth-server")
    private String uri;

    /**
     * 路由断言
     */
    @NotNull(message = "路由断言不能为空")
    @Schema(description = "路由断言", requiredMode = Schema.RequiredMode.REQUIRED)
    private String predicates;

    /**
     * 路由过滤器
     */
    @NotNull(message = "路由过滤器不能为空")
    @Schema(description = "路由过滤器", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String filters;

    /**
     * 路由元数据
     */
    @Schema(description = "路由元数据", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String metadata;

    /**
     * 路由描述
     */
    @Schema(description = "路由描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;

    /**
     * 路由状态
     *
     * @see CommonStatusEnum
     */
    @NotNull(message = "路由状态不能为空")
    @Schema(description = "路由状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
    private Integer status;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    @Min(value = 0, message = "排序不能小于0")
    @Schema(description = "排序", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
    private Integer sort;

}
