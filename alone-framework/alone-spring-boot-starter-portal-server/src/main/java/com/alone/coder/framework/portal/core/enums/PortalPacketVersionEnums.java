package com.alone.coder.framework.portal.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PortalPacketVersionEnums {

    /**
     * 移动 V1标准
     */
    CMCCV1_TYPE(0x01, "cmccV1"),
    /**
     * 移动 V2标准
     */
    CMCCV2_TYPE(0x02, "cmccV2"),
    /**
     * 华为 V1标准
     */
    HUAWEIV1_TYPE(0x01, "huaweiV1"),
    /**
     * 华为 V2标准
     */
    HUAWEIV2_TYPE(0x02, "huaweiV2");


    private final int value;

    private final String name;
}
