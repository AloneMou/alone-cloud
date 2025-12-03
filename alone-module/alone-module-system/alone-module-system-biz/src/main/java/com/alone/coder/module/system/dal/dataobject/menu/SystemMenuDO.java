package com.alone.coder.module.system.dal.dataobject.menu;

import com.alone.coder.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单DO
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "system_menu", autoResultMap = true)
public class SystemMenuDO extends BaseDO {

    @TableId
    private Long id;


}
