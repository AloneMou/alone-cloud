package com.alone.coder.framework.file.core.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alone.coder.framework.common.util.string.StrUtils;
import com.alone.coder.framework.file.core.error.exception.InitializeStorageSourceException;
import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;
import com.alone.coder.framework.file.core.module.param.LocalParam;
import com.alone.coder.framework.file.core.service.base.AbstractBaseFileService;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;

import static cn.hutool.core.io.FileUtil.PATH_SEPARATOR;
import static com.alone.coder.framework.file.core.error.code.StorageErrorCode.STORAGE_SOURCE_FILE_GET_ITEM_FAIL;
import static com.alone.coder.framework.file.core.error.code.StorageErrorCode.STORAGE_SOURCE_INIT_FAIL;

/**
 * @author zhaojun
 */
@Slf4j
public class LocalServiceImpl extends AbstractBaseFileService<LocalParam> {

    public LocalServiceImpl(String configId, LocalParam param) {
        super(configId, param);
    }

    @Override
    public void init() {
        // 初始化存储源
        File file = new File(param.getFilePath());
        // 校验文件夹是否存在
        if (!file.exists()) {
            String errMsg = StrUtil.format("文件路径:「{}」不存在, 请检查是否填写正确.", file.getAbsolutePath());
            throw new InitializeStorageSourceException(STORAGE_SOURCE_INIT_FAIL, errMsg);
        }
    }

    @Override
    public StorageTypeEnum getStorageTypeEnum() {
        return StorageTypeEnum.LOCAL;
    }

    @Override
    public String upload(byte[] content, String path, String type) throws Exception {
        String year = DateUtil.format(new Date(), DatePattern.NORM_YEAR_PATTERN);
        String month = DateUtil.format(new Date(), "MM");
        String day = DateUtil.format(new Date(), "dd");
        // 拼接路径=> /2023/01/01/xxx.png
        path = StrUtils.concat(param.getFilePath(), year, "/", month, "/", day, "/", path);
        // 去除开头的 /
        path = StrUtils.removeDuplicateSlashes(path);
        checkPathSecurity(path);
        // 如果目录不存在则创建
        String parentPath = FileUtil.getParent(path, 1);
        if (!FileUtil.exist(parentPath)) {
            FileUtil.mkdir(parentPath);
        }


        InputStream in = new ByteArrayInputStream(content);
        File uploadToFileObj = new File(path);
        BufferedOutputStream outputStream = FileUtil.getOutputStream(uploadToFileObj);
        IoUtil.copy(in, outputStream);
        IoUtil.close(outputStream);
        IoUtil.close(in);
        // 拼接返回路径
        return param.getDomain() + "/" + path;
    }

    @Override
    public String getDownloadUrl(String pathAndName) {
        return param.getDomain() + "/" + pathAndName;
    }

    @Override
    public boolean newFolder(String path, String name) {
        checkPathSecurity(path);
        checkNameSecurity(name);
        String fullPath = StrUtils.concat(param.getFilePath(), path, name);
        return FileUtil.mkdir(fullPath) != null;
    }

    @Override
    public boolean deleteFile(String path) {
        checkPathSecurity(path);
        String fullPath = StrUtils.concat(param.getFilePath(), path);
        return FileUtil.del(fullPath);
    }

    @Override
    public byte[] getContent(String path) {
        checkPathSecurity(path);
        File file = new File(StrUtils.removeDuplicateSlashes(param.getFilePath() + PATH_SEPARATOR + path));
        if (!file.exists()) {
            throw new InitializeStorageSourceException(STORAGE_SOURCE_FILE_GET_ITEM_FAIL, "文件不存在或异常，请联系管理员.");
        }
        return FileUtil.readBytes(file);
    }


    /**
     * 检查路径合法性：
     * - 只有以 . 开头的允许通过，其他的如 ./ ../ 的都是非法获取上层文件夹内容的路径.
     *
     * @param paths 文件路径
     * @throws IllegalArgumentException 文件路径包含非法字符时会抛出此异常
     */
    private static void checkPathSecurity(String... paths) {
        for (String path : paths) {
            // 路径中不能包含 .. 不然可能会获取到上层文件夹的内容
            if (StrUtil.startWith(path, "/..") || StrUtil.containsAny(path, "../", "..\\")) {
                throw new IllegalArgumentException("文件路径存在安全隐患: " + path);
            }
        }
    }


    /**
     * 检查路径合法性：
     * - 不为空，且不包含 \ / 字符
     *
     * @param names 文件路径
     * @throws IllegalArgumentException 文件名包含非法字符时会抛出此异常
     */
    private static void checkNameSecurity(String... names) {
        for (String name : names) {
            // 路径中不能包含 .. 不然可能会获取到上层文件夹的内容
            if (StrUtil.containsAny(name, "\\", "/")) {
                throw new IllegalArgumentException("文件名存在安全隐患: " + name);
            }
        }
    }
}