package com.alone.coder.file.controller.admin.file.vo.config;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;


/**
 * @author AgoniMou
 */
@Data
public class StorageConfigBaseVO {

    @Schema(description = "配置名", requiredMode = Schema.RequiredMode.REQUIRED, example = "S3 - 阿里云")
    @NotNull(message = "配置名不能为空")
    private String name;

    @Schema(description = "备注", example = "我是备注")
    private String remark;
}
