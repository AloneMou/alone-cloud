package com.alone.coder.framework.security.core.enums;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum GrantTypeEnums {

    GRANT_DEFAULTS("DEFAULTS", "默认"),
    SMS("SMS", "短信授权方式"),
    ;

    @Getter
    private final String type;

    private final String desc;

    public static GrantTypeEnums create(String type) {
        for (GrantTypeEnums grantTypeEnums : GrantTypeEnums.values()) {
            if (grantTypeEnums.getType().equals(type)) {
                return grantTypeEnums;
            }
        }
        return GRANT_DEFAULTS;
    }

}
