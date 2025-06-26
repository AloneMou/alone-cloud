package com.alone.coder.file.service.file;


import com.alone.coder.file.controller.admin.file.vo.config.StorageConfigCreateReqVO;
import com.alone.coder.file.controller.admin.file.vo.config.StorageConfigPageReqVO;
import com.alone.coder.file.controller.admin.file.vo.config.StorageConfigUpdateReqVO;
import com.alone.coder.file.dal.dataobject.file.StorageConfig;
import com.alone.coder.framework.common.pojo.PageResult;
import com.alone.coder.framework.file.core.service.base.BaseFileService;
import jakarta.validation.Valid;

/**
 * @author AgoniMou
 */
public interface StorageConfigService {

    /**
     * 初始化文件客户端
     */
    void initLocalCache();

    /**
     * 分页查询文件配置
     *
     * @param reqVO 筛选条件
     * @return 文件配置分页
     */
    PageResult<StorageConfig> getStorageConfigPage(StorageConfigPageReqVO reqVO);

    /**
     * 创建文件配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createFileConfig(@Valid StorageConfigCreateReqVO createReqVO);

    /**
     * 更新文件配置
     *
     * @param updateReqVO 更新信息
     */
    void updateFileConfig(@Valid StorageConfigUpdateReqVO updateReqVO);

    /**
     * 更新文件配置为 Master
     *
     * @param id 编号
     */
    void updateFileConfigMaster(String id);

    /**
     * 删除文件配置
     *
     * @param id 编号
     */
    void deleteFileConfig(String id);

    /**
     * 获得文件配置
     *
     * @param id 编号
     * @return 文件配置
     */
    StorageConfig getStorageConfig(String id);

    /**
     * 测试文件配置是否正确，通过上传文件
     *
     * @param id 编号
     * @return 文件 URL
     */
    String testFileConfig(String id) throws Exception;

    /**
     * 获得指定编号的文件客户端
     *
     * @param id 配置编号
     * @return 文件客户端
     */
    BaseFileService getFileClient(String id);

    /**
     * 获得 Master 文件客户端
     *
     * @return 文件客户端
     */
    BaseFileService getMasterFileClient();


}
