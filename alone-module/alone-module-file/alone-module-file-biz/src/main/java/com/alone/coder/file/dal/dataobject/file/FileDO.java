package com.alone.coder.file.dal.dataobject.file;

import com.alone.coder.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.awt.geom.Area;

/**
 * 文件表
 * 每次文件上传，都会记录一条记录到该表中
 *
 * @author AgoniMou
 */
@TableName(value = "infra_file",autoResultMap = true)
@KeySequence("infra_file_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDO extends BaseDO {

    /**
     * 编号，数据库自增
     */
    @TableId
    private String id;

    /**
     * 配置编号
     * <p>
     * 关联 {@link StorageConfig#getId()}
     */
    private String configId;

    /**
     * 原文件名
     */
    private String name;

    /**
     * 路径，即文件名
     */
    private String path;

    /**
     * 访问地址
     */
    private String url;

    /**
     * 文件的 MIME 类型，例如 "application/octet-stream"
     */
    private String type;

    /**
     * 文件大小
     */
    private Integer size;

    /**
     * 文件拓展名
     */
    private String extension;


    /**
     * 文档类型
     * 枚举{@link DocumentTypeEnum}
     */
    private String documentType;

    /**
     * 文件上传的 IP
     */
    private String ip;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Area area;
}
