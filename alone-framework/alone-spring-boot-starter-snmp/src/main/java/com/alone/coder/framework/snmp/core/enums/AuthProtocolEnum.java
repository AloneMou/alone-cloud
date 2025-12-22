package com.alone.coder.framework.snmp.core.enums;

import com.alone.coder.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.snmp4j.security.*;
import org.snmp4j.smi.OID;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AuthProtocolEnum implements ArrayValuable<String> {

    MD5("MD5", AuthMD5.ID),
    SHA("SHA", AuthSHA.ID), // SHA-1
    SHA224("SHA224", AuthHMAC128SHA224.ID),
    SHA256("SHA256", AuthHMAC192SHA256.ID),
    SHA384("SHA384", AuthHMAC256SHA384.ID),
    SHA512("SHA512", AuthHMAC384SHA512.ID);

    private final String name;
    private final OID oid;

    public static final String[] ARRAYS = Arrays.stream(values()).map(AuthProtocolEnum::getName).toArray(String[]::new);


    public static AuthProtocolEnum fromName(String name) {
        if (name == null) {
            return null;
        }
        for (AuthProtocolEnum protocol : values()) {
            if (protocol.name.equalsIgnoreCase(name)) {
                return protocol;
            }
        }
        return null;
    }



    @Override
    public String[] array() {
        return ARRAYS;
    }
}
