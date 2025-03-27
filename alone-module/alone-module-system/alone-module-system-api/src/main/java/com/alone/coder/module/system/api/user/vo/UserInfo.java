package com.alone.coder.module.system.api.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息
 */
@Data
public class UserInfo implements Serializable {

    /**
     * 用户编号
     */
    @Schema(description = "用户编号", example = "1")
    private Long id;

    /**
     * 用户账号
     */
    @Schema(description = "用户账号", example = "admin")
    private String username;

    /**
     * 用户密码
     */
    @Schema(description = "用户密码", example = "123456")
    private String password;

    /**
     * 用户状态 0-停用 1-正常
     */
    @Schema(description = "用户状态", example = "1")
    private Integer status;

    /**
     * 用户类型
     * <p>
     */
    @Schema(description = "用户类型", example = "1")
    private Integer userType;

    /**
     * 权限标识集合
     */
    @Schema(description = "权限标识集合")
    private List<String> permissions;

    /**
     * 授权范围
     */
    @Schema(description = "授权范围")
    private List<String> scopes;

}
