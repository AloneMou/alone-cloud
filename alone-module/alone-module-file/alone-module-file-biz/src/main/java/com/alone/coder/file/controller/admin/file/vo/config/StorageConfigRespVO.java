package com.alone.coder.file.controller.admin.file.vo.config;

import com.alone.coder.framework.file.core.module.param.IStorageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author AgoniMou
 */
@Schema(description = "管理后台 - 文件配置 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StorageConfigRespVO extends StorageConfigBaseVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String id;

    @Schema(description = "存储器,参见 FileStorageEnum 枚举类", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "存储器不能为空")
    private String storage;

    @Schema(description = "是否为主配置", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "是否为主配置不能为空")
    private Boolean master;

    @Schema(description = "存储配置", requiredMode = Schema.RequiredMode.REQUIRED)
    private IStorageParam config;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;
}
