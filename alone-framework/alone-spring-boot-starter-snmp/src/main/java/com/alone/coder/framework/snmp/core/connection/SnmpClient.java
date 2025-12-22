package com.alone.coder.framework.snmp.core.connection;

import org.snmp4j.Snmp;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

import java.io.IOException;
import java.util.List;

public interface SnmpClient {



    /**
     * 获取单个OID的值
     *
     * @param oid OID
     * @return VariableBinding
     * @throws IOException 获取失败
     */
    default VariableBinding get(String oid) throws IOException {
        return get(new OID(oid));
    }

    /**
     * 获取单个OID的值
     */
    VariableBinding get(OID oid) throws IOException;

    List<VariableBinding> get(List<OID> oids) throws IOException;

    List<VariableBinding> getStr(List<String> oids) throws IOException;


    /**
     * 遍历某个OID子树，返回所有VariableBinding列表
     *
     * @param rootOid 根OID
     * @return VariableBinding列表
     * @throws IOException 获取失败
     */
    default List<VariableBinding> walk(String rootOid) throws IOException {
        return walk(new OID(rootOid));
    }

    /**
     * 通过walk遍历某个OID子树，返回所有VariableBinding列表
     */
    List<VariableBinding> walk(OID rootOid) throws IOException;

    /**
     * 设置指定OID的值
     */
    void set(VariableBinding varBind) throws IOException;

    /**
     * 关闭客户端，释放资源
     */
    void close() throws IOException;

    /**
     * 返回底层Snmp实例，便于高级操作
     */
    Snmp getSnmp();

    /**
     * 获取当前目标地址
     */
    String getTargetAddress();

    /**
     * 获取当前SNMP版本号，方便外部判断
     */
    int getVersion();

    /**
     * 刷新重新连接
     */
    void refresh() throws IOException;
}
