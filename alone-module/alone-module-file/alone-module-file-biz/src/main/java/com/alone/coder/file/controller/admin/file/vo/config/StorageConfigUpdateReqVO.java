package com.alone.coder.file.controller.admin.file.vo.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

/**
 * @author AgoniMou
 */
@Schema(description = "管理后台 - 文件配置更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StorageConfigUpdateReqVO extends StorageConfigBaseVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "编号不能为空")
    private String id;

    @Schema(description = "存储配置,配置是动态参数，所以使用 Map 接收", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "存储配置不能为空")
    private Map<String, Object> config;
}
