package com.alone.coder.framework.snmp.core.service;

import com.alone.coder.framework.snmp.core.pojo.SnmpSetting;
import org.snmp4j.smi.VariableBinding;

import java.util.List;

public interface SnmpSettingApi {

    /**
     * 查询可用的SNMP设置
     */
    List<SnmpSetting> getAvailableSnmpSetting();

    /**
     * 发送TRAP消息
     *
     * @param ip               来源IP
     * @param variableBindings 绑定值
     */
    void sendTrap(String ip, List<VariableBinding> variableBindings);
}
