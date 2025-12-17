package com.alone.coder.framework.file.core.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.ftp.Ftp;
import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;
import com.alone.coder.framework.file.core.module.param.FtpParam;
import com.alone.coder.framework.file.core.service.base.AbstractProxyTransferService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.*;

/**
 * @author zhaojun
 */
@Service
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FtpServiceImpl extends AbstractProxyTransferService<FtpParam> {


    @Override
    public void uploadFile(String pathAndName, InputStream inputStream, Long size) throws Exception {

    }

    @Override
    public void init() {

    }

    @Override
    public StorageTypeEnum getStorageTypeEnum() {
        return null;
    }

    @Override
    public String upload(byte[] content, String path, String type) throws Exception {
        return "";
    }

    @Override
    public String getDownloadUrl(String pathAndName) {
        return "";
    }

    @Override
    public boolean newFolder(String path, String name) {
        return false;
    }

    @Override
    public boolean deleteFile(String path) throws Exception {
        return false;
    }

    @Override
    public byte[] getContent(String path) throws Exception {
        return new byte[0];
    }
}