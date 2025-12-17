package com.alone.coder.framework.file.core.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpException;
import cn.hutool.extra.ftp.FtpMode;
import com.alone.coder.framework.common.util.string.StrUtils;
import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;
import com.alone.coder.framework.file.core.module.param.FtpParam;
import com.alone.coder.framework.file.core.service.base.AbstractProxyTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author zhaojun
 */
@Service
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FtpServiceImpl extends AbstractProxyTransferService<FtpParam> {

    private Ftp ftp;

    public FtpServiceImpl(String storageId, FtpParam param) {
        super(storageId, param);
    }


    @Override
    public void init() {
        FtpConfig config = new FtpConfig(param.getHost(), param.getPort(), param.getUsername(), param.getPassword(), Charset.forName(param.getEncoding()));
        config.setSoTimeout(DEFAULT_CONNECTION_TIMEOUT_MILLIS);
        this.ftp = new Ftp(config, FtpMode.valueOf(param.getFtpMode()));
    }

    @Override
    public StorageTypeEnum getStorageTypeEnum() {
        return StorageTypeEnum.FTP;
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
        InputStream in = new ByteArrayInputStream(content);
        boolean success = ftp.upload(path, fileName, in);
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
        boolean success = ftp.mkdir(fullPath);
        if (!success) {
            throw new FtpException(StrUtil.format("创建目录 ({}) 失败", fullPath));
        }
        return true;
    }

    @Override
    public boolean deleteFile(String path) throws Exception {
        String fullPath = StrUtils.concat(param.getBasePath(), path);
        fullPath = StrUtils.trimStartSlashes(fullPath);
        reconnectIfTimeout();
        boolean success = ftp.delFile(fullPath);
        if (!success) {
            throw new FtpException(StrUtil.format("删除文件 ({}) 失败", fullPath));
        }
        return true;
    }

    @Override
    public byte[] getContent(String path) throws Exception {
        String fullPath = StrUtils.concat(param.getBasePath(), path);
        fullPath = StrUtils.trimStartSlashes(fullPath);

        String fileName = FileUtil.getName(fullPath);
        String dir = StrUtil.removeSuffix(fullPath, fileName);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        reconnectIfTimeout();
        ftp.download(dir, fileName, out);
        return out.toByteArray();
    }

    @Override
    public String getUploadUrl(String path) {
        return "";
    }

    private synchronized void reconnectIfTimeout() {
        ftp.reconnectIfTimeout();
    }
}