package com.alone.coder.framework.snmp.core.connection;

import com.alone.coder.framework.snmp.core.enums.SnmpVersionEnum;
import org.snmp4j.smi.OID;

import java.io.IOException;

public interface SnmpClientFactory {

    /**
     * 获取客户端
     *
     * @param id 客户端 id
     * @return SnmpClient
     */
    SnmpClient getClient(Long id);

    /**
     * 删除客户端
     *
     * @param id 客户端 id
     */
    void removeClient(Long id);

    /**
     * 创建v1/v2c客户端
     *
     * @param version   V1或V2C
     * @param address   目标地址，比如 "udp:127.0.0.1/161"
     * @param community 社区字符串
     */
    SnmpClient createClient(Long id, SnmpVersionEnum version, String address, String community) throws IOException;

    /**
     * 创建v3客户端，需传入安全配置
     *
     * @param address        目标地址，比如 "udp:127.0.0.1/161"
     * @param username       用户名
     * @param authProtocol   认证协议 (OID)，如 AuthMD5.ID，允许传null
     * @param authPassphrase 认证密码
     * @param privProtocol   加密协议 (OID)，如 PrivAES128.ID，允许传null
     * @param privPassphrase 加密密码
     */
    SnmpClient createV3Client(Long id, String address,
                              String username,
                              OID authProtocol,
                              String authPassphrase,
                              OID privProtocol,
                              String privPassphrase) throws IOException;
}
