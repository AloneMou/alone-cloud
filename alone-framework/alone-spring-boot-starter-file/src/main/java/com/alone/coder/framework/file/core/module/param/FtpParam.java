package com.alone.coder.framework.file.core.module.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 本地存储初始化参数
 *
 * @author zhaojun
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FtpParam extends ProxyTransferParam {

    /**
     * 域名或 IP
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 编码格式
     * <p>
     * 表示文件夹及文件名称的编码格式，不表示文本内容的编码格式.
     * </p>
     */
    private String encoding = "UTF-8";

    /**
     * 用户名
     * <p>
     * 如果是匿名访问，不填写保存失败的话，可能用户名需要写 anonymous
     * </p>
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 基路径
     * <p>
     * 基路径表示该存储源哪个目录在 ZFile 中作为根目录，如： '/'，'/文件夹1'
     * </p>
     */
    private String basePath = "/";

    /**
     * 域名
     */
    private String domain;

    /**
     * FTP 模式
     * <p>
     * 主动模式为 FTP 服务端主动连接客户端(随机开放端口，需保证防火墙无限制端口)，被动模式为 FTP 服务端被动等待客户端连接.
     * </p>
     */
    private String ftpMode = "passive";

}