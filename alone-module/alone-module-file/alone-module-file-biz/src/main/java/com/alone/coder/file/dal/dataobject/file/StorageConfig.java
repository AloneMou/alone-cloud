package com.alone.coder.file.dal.dataobject.file;

import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;
import com.alone.coder.framework.file.core.module.param.IStorageParam;
import com.alone.coder.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 文件配置表
 *
 * @author AgoniMou
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "infra_storage_config", autoResultMap = true)
public class StorageConfig extends BaseDO {

    /**
     * 配置编号，数据库自增
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 配置名
     */
    private String name;

    /**
     * 存储器
     * <p>
     * 枚举 {@link StorageTypeEnum#getKey()}
     */
    private String storage;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否为主配置
     * <p>
     * 由于我们可以配置多个文件配置，默认情况下，使用主配置进行文件的上传
     * </p>
     */
    private Boolean master;

    /**
     * 存储器配置
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private IStorageParam config;
}
