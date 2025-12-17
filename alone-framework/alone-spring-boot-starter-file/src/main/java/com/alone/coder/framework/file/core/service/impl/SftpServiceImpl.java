package com.alone.coder.framework.file.core.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpException;
import cn.hutool.extra.ssh.JschRuntimeException;
import cn.hutool.extra.ssh.Sftp;
import com.alone.coder.framework.common.util.string.StrUtils;
import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;
import com.alone.coder.framework.file.core.module.param.SftpParam;
import com.alone.coder.framework.file.core.service.base.AbstractProxyTransferService;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author zhaojun
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class SftpServiceImpl extends AbstractProxyTransferService<SftpParam> {

    private Sftp sftp;

    static {
        // 忽略公钥校验
        JSch.setConfig("StrictHostKeyChecking", "no");
        // 某些旧的 sftp 服务器仅支持 ssh-dss 协议，该协议并不安全，默认不支持该协议，按需添加
        JSch.setConfig("server_host_key", JSch.getConfig("server_host_key") + ",ssh-dss");
    }

    public SftpServiceImpl(String storageId, SftpParam param) {
        super(storageId, param);
    }

    @Override
    public void init() {
        try {
            // 密码登录
            JSch jsch = new JSch();
            Session session = jsch.getSession(param.getUsername(), param.getHost(), param.getPort());
            session.setTimeout(DEFAULT_CONNECTION_TIMEOUT_MILLIS);
            if (StrUtil.isBlank(param.getPrivateKey())) {
                session.setPassword(param.getPassword());
            } else {
                byte[] passphraseBytes = null;
                if (param.getPassphrase() != null && !param.getPassphrase().isEmpty()) {
                    passphraseBytes = param.getPassphrase().getBytes(StandardCharsets.UTF_8);
                }
                jsch.addIdentity(param.getUsername(), param.getPrivateKey().getBytes(StandardCharsets.UTF_8), null, passphraseBytes);
            }
            this.sftp = new Sftp(session, Charset.forName(param.getEncoding()), DEFAULT_CONNECTION_TIMEOUT_MILLIS);
            log.debug("Creating object: {}", sftp);
        } catch (Exception e) {
            log.error("SFTP初始化失败", e);
        }
    }

    @Override
    public StorageTypeEnum getStorageTypeEnum() {
        return StorageTypeEnum.SFTP;
    }

    @Override
    public String upload(byte[] content, String path, String type) throws Exception {
        Date date = new Date();
        String year = DateUtil.format(date, DatePattern.NORM_YEAR_PATTERN);
        String month = DateUtil.format(date, "MM");
        String day = DateUtil.format(date, "dd");
        // 拼接路径=> /2023/01/01/xxx.png
        path = StrUtils.concat(param.getBasePath(), year, "/", month, "/", day, "/", path);
        // 去除开头的 /
        path = StrUtils.trimStartSlashes(path);
        String fileName = FileUtil.getName(path);
        reconnectIfTimeout();
        InputStream in = new ByteArrayInputStream(content);
        boolean success = sftp.upload(path, fileName, in);
        log.debug("上传文件成功: {}", path);
        if (!success) {
            throw new FtpException(StrUtil.format("上传文件到目标目录 ({}) 失败", path));
        }
        IoUtil.close(in);
        // 拼接返回路径
        return param.getDomain() + StrUtils.PATH_SEPARATOR_CHAR + path;
    }

    @Override
    public String getDownloadUrl(String pathAndName) {
        String fullPath = StrUtils.concatTrimStartSlashes(param.getBasePath() + pathAndName);
        return StrUtil.isNotEmpty(param.getDomain()) ? StrUtils.concat(param.getDomain(), fullPath) : fullPath;
    }

    @Override
    public boolean newFolder(String path, String name) {
        name = StrUtils.trimSlashes(name);
        String fullPath = StrUtils.concat(param.getBasePath(), path, name, StrUtils.DELIMITER_STR);
        fullPath = StrUtils.trimStartSlashes(fullPath);
        reconnectIfTimeout();
        boolean success = sftp.mkdir(fullPath);
        if (!success) {
            throw new JschRuntimeException(StrUtil.format("创建目录 ({}) 失败", fullPath));
        }
        return true;
    }

    @Override
    public boolean deleteFile(String path) throws Exception {
        String fullPath = StrUtils.concat(param.getBasePath(), path);
        fullPath = StrUtils.trimStartSlashes(fullPath);
        reconnectIfTimeout();
        boolean success = sftp.delFile(fullPath);
        if (!success) {
            throw new JschRuntimeException(StrUtil.format("删除文件 ({}) 失败", fullPath));
        }
        return true;
    }

    @Override
    public byte[] getContent(String path) throws Exception {
        String fullPath = StrUtils.concat(param.getBasePath(), path);
        fullPath = StrUtils.trimStartSlashes(fullPath);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        reconnectIfTimeout();
        sftp.getClient().get(fullPath, out);
        return out.toByteArray();
    }

    @Override
    public String getUploadUrl(String path) {
        return "";
    }


    private synchronized void reconnectIfTimeout() {
        sftp.reconnectIfTimeout();
    }
}