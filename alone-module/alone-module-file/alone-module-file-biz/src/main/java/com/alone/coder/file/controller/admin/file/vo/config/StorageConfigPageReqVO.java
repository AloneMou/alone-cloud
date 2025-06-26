package com.alone.coder.file.controller.admin.file.vo.config;

import com.alone.coder.framework.common.pojo.PageParam;
import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author AgoniMou
 * @date 2024/1/9
 */
@Schema(description = "管理后台 - 文件配置分页查询 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StorageConfigPageReqVO extends PageParam {

    /**
     * 配置名
     */
    @Schema(description = "配置名", requiredMode = Schema.RequiredMode.REQUIRED, example = "S3 - 阿里云")
    private String name;

    /**
     * 存储器
     * <p>
     * 枚举 {@link StorageTypeEnum#getKey()}
     */
    @Schema(description = "存储器,参见 StorageTypeEnum 枚举类", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String storage;
}
