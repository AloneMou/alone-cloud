package com.alone.coder.file.service.file.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

import com.alone.coder.file.controller.admin.file.vo.file.FilePageReqVO;
import com.alone.coder.file.dal.dataobject.file.FileDO;
import com.alone.coder.file.dal.mysql.file.FileMapper;
import com.alone.coder.file.service.file.FileService;
import com.alone.coder.file.service.file.StorageConfigService;
import com.alone.coder.framework.common.pojo.PageResult;
import com.alone.coder.framework.common.util.io.FileUtils;
import com.alone.coder.framework.common.util.servlet.ServletUtils;
import com.alone.coder.framework.file.core.service.base.BaseFileService;
import com.alone.coder.framework.file.core.utils.FileTypeUtils;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import static com.alone.coder.file.enums.ErrorCodeConstants.FILE_NOT_EXISTS;
import static com.alone.coder.framework.common.exception.util.ServiceExceptionUtil.exception;


/**
 * 文件 Service 实现类
 *
 * @author AgoniMou
 */
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private StorageConfigService storageConfigService;

    @Resource
    private FileMapper fileMapper;

    @Override
    public PageResult<FileDO> getFilePage(FilePageReqVO pageReqVO) {
        return fileMapper.selectPage(pageReqVO);
    }

    @Override
    @SneakyThrows
    public String createFile(String name, String path, byte[] content) {
        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (StrUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StrUtil.isEmpty(name)) {
            name = path;
        }
        String extension = FileUtil.getSuffix(name);
        // 上传到文件存储器
        BaseFileService client = storageConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type);

        // 保存到数据库
        FileDO file = new FileDO();
        file.setConfigId(client.getStorageId());
        file.setName(name);
        file.setPath(path);
        file.setUrl(url);
        file.setType(type);
        file.setSize(content.length);
        file.setExtension(extension);
        file.setIp(ServletUtils.getClientIP());
//        file.setArea(IPUtils.getArea(file.getIp()));
//        file.setDocumentType(DocumentTypeEnum.matching(extension).name());
        fileMapper.insert(file);
        return url;
    }

    @Override
    public void deleteFile(String id) throws Exception {
        // 校验存在
        FileDO file = validateFileExists(id);

        // 从文件存储器中删除
        BaseFileService client = storageConfigService.getFileClient(file.getConfigId());
        Assert.notNull(client, "客户端({}) 不能为空", file.getConfigId());
        client.deleteFile(file.getPath());
        // 删除记录
        fileMapper.deleteById(id);
    }

    private FileDO validateFileExists(String id) {
        FileDO fileDO = fileMapper.selectById(id);
        if (fileDO == null) {
            throw exception(FILE_NOT_EXISTS);
        }
        return fileDO;
    }

    @Override
    public byte[] getFileContent(String configId, String path) throws Exception {
        BaseFileService client = storageConfigService.getFileClient(configId);
        Assert.notNull(client, "客户端({}) 不能为空", configId);
        return client.getContent(path);
    }

}
