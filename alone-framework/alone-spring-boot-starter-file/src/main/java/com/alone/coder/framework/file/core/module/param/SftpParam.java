package com.alone.coder.framework.file.core.module.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SFTP 初始化参数
 *
 * @author zhaojun
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SftpParam extends ProxyTransferParam {

    /**
     * 主机地址
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 编码格式
     * <p>表示文件夹及文件名称的编码格式，不表示文本内容的编码格式.</p>
     */
    private String encoding = "UTF-8";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 密钥
     */
    private String privateKey;

    /**
     * 密钥 passphrase
     */
    private String passphrase;

    /**
     * 基路径
     */
    private String basePath = "/";

    /**
     * 最大连接数
     * <p>要确保你服务器 SSH 的可用连接数大于这个值，不然可能会报错 channel is not opened.</p>
     */
    private Integer maxConnections;

}