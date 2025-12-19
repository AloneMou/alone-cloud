package com.alone.coder.framework.portal.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PortalAuthTypeEnums {

    /**
     * CHAP认证
     */
    AUTH_CHAP(0x00, "CHAP"),
    /**
     * PAP认证
     */
    AUTH_PAP(0x01, "PAP");

    /**
     * 认证方式
     */
    @Getter
    private final int value;

    /**
     * 认证方式名称
     */
    private final String name;

    public static PortalAuthTypeEnums fromValue(int value) {
        for (PortalAuthTypeEnums type : PortalAuthTypeEnums.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}
