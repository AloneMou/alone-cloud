package com.alone.coder.framework.snmp.core.pojo;

import lombok.Data;

@Data
public class SnmpSetting {

    /**
     * 设备ID
     */
    private Long id;

    /**
     * 设备IP
     */
    private String ip;

    /*
     * 设备端口
     */
    private Integer port;

    /**
     * 设备版本
     */
    private Integer version; // v1, v2c, v3

    /**
     * 团体名
     */
    private String community;

    /**
     * 用户名
     */
    private String username;

    /**
     * 认证协议
     */
    private String authProtocol;

    /**
     * 认证密码
     */
    private String authPassphrase;

    /**
     * 加密协议
     */
    private String privProtocol;

    /**
     * 加密密码
     */
    private String privPassphrase;
}
