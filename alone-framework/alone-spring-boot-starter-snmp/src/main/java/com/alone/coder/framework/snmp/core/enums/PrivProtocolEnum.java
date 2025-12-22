package com.alone.coder.framework.snmp.core.enums;

import com.alone.coder.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.snmp4j.security.PrivAES128;
import org.snmp4j.security.PrivAES192;
import org.snmp4j.security.PrivAES256;
import org.snmp4j.security.PrivDES;
import org.snmp4j.smi.OID;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PrivProtocolEnum implements ArrayValuable<String> {


    DES("DES", PrivDES.ID),
    AES128("AES128", PrivAES128.ID),
    AES192("AES192", PrivAES192.ID),
    AES256("AES256", PrivAES256.ID);;

    private final String name;
    private final OID oid;
    public static final String[] ARRAYS = Arrays.stream(values()).map(PrivProtocolEnum::getName).toArray(String[]::new);

    @Override
    public String[] array() {
        return ARRAYS;
    }


    public static PrivProtocolEnum fromName(String name) {
        if (name == null) return null;
        for (PrivProtocolEnum protocol : values()) {
            if (protocol.name.equalsIgnoreCase(name)) {
                return protocol;
            }
        }
        return null; // 未匹配到
    }
}
