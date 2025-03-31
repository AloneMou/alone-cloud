package com.alone.coder.module.system.dal.dataobject.route;

import com.alone.coder.framework.common.enums.CommonStatusEnum;
import com.alone.coder.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "system_route", autoResultMap = true)
public class SystemRouteDO extends BaseDO {

    /**
     * 路由ID
     */
    @TableId
    private Long id;

    /**
     * 路由名称
     */
    private String name;

    /**
     * 路由路径
     */
    private String uri;

    /**
     * 路由断言
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<PredicateDefinition> predicates;

    /**
     * 路由过滤器
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<PredicateDefinition> filters;

    /**
     * 路由元数据
     */
    private String metadata;

    /**
     * 路由描述
     */
    private String description;

    /**
     * 路由状态
     *
     * @see CommonStatusEnum
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;




    @Data
    public static  class PredicateDefinition{

        private String name;

        private Map<String, String> args = new LinkedHashMap<>();
    }

}
