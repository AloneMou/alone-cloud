package com.alone.coder.module.system.api.oauth2.vo;

import com.alone.coder.framework.common.enums.CommonStatusEnum;
import com.alone.coder.module.system.enums.oauth2.OAuth2GrantTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class SystemOauthClientRespDTO {

    /**
     * 客户端ID
     */
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
    private List<String> redirectUris;

    /**
     * 授权类型（模式）
     * <p>
     * 枚举 {@link OAuth2GrantTypeEnum}
     */
    private List<String> authorizedGrantTypes;
    /**
     * 授权范围
     */
    private List<String> scopes;

    /**
     * 是否自动授权
     */
    private Boolean autoApprove;



}
