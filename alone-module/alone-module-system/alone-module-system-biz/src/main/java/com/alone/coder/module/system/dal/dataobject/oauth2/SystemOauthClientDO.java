package com.alone.coder.module.system.dal.dataobject.oauth2;

import com.alone.coder.framework.common.enums.CommonStatusEnum;
import com.alone.coder.framework.mybatis.core.dataobject.BaseDO;
import com.alone.coder.module.system.enums.oauth2.OAuth2GrantTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "system_oauth_client", autoResultMap = true)
public class SystemOauthClientDO extends BaseDO {

    /**
     * 客户端ID
     */
    @TableId
    private Long id;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;


    /**
     * 访问令牌的有效期
     */
    private Integer accessTokenValiditySeconds;
    /**
     * 刷新令牌的有效期
     */
    private Integer refreshTokenValiditySeconds;

    /**
     * 状态
     * <p>
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;


    /**
     * 扩展信息
     */
    private String additionalInformation;

    /**
     * 资源ID
     */
    private String resourceIds;

    /**
     * 可重定向的 URI 地址
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> redirectUris;

    /**
     * 授权类型（模式）
     * <p>
     * 枚举 {@link OAuth2GrantTypeEnum}
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> authorizedGrantTypes;
    /**
     * 授权范围
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> scopes;


    /**
     * 是否自动放行
     */
    private Boolean autoApprove;
    /**
     * 描述
     */
    private String remark;


}
