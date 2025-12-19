package com.alone.coder.framework.portal.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PortalAttributeTypeEnums {

    /**
     * mac地址
     */
    ATTRIBUTE_MAC_TYPE(0xff),
    /**
     * 用户名
     */
    ATTRIBUTE_USERNAME_TYPE(0x01),
    /**
     * 密码
     */
    ATTRIBUTE_PASSWORD_TYPE(0x02),

    /**
     * 响应类型
     */
    ATTRIBUTE_CHALLENGE_TYPE(0x03),
    /**
     * CHAP密码
     */
    ATTRIBUTE_CHAP_PWD_TYPE(0x04),
    /**
     * 文本信息
     */
    ATTRIBUTE_TEXT_INFO_TYPE(0x05),
    /**
     * 上行速度
     */
    ATTRIBUTE_UP_LINK_TYPE(0x06),
    /**
     * 下行速度
     */
    ATTRIBUTE_DOWN_LINK_TYPE(0x07),
    /**
     * 端口
     */
    ATTRIBUTE_PORT_TYPE(0x08),
    /**
     * BASIP
     */
    ATTRIBUTE_BASIP_TYPE(0x0a);


    private final int value;


}
