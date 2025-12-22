package com.alone.coder.framework.snmp.core.enums;

import com.alone.coder.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SnmpVersionEnum implements ArrayValuable<Integer> {
    SNMP_V1(0, "SNMP V1"),
    SNMP_V2C(1, "SNMP V2C"),
    SNMP_V3(3, "SNMP V3");

    private final Integer value;
    private final String name;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(SnmpVersionEnum::getValue).toArray(Integer[]::new);


    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static SnmpVersionEnum fromValue(Integer value) {
        for (SnmpVersionEnum item : values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
        return null;
    }
}
