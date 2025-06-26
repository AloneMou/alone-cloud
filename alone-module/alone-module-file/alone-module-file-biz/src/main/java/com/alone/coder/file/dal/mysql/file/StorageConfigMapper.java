package com.alone.coder.file.dal.mysql.file;


import com.alone.coder.file.controller.admin.file.vo.config.StorageConfigPageReqVO;
import com.alone.coder.file.dal.dataobject.file.StorageConfig;
import com.alone.coder.framework.common.pojo.PageResult;
import com.alone.coder.framework.mybatis.core.mapper.BaseMapperX;
import com.alone.coder.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author AgoniMou
 */
@Mapper
public interface StorageConfigMapper extends BaseMapperX<StorageConfig> {

    default PageResult<StorageConfig> getStorageConfigPage(StorageConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<StorageConfig>()
                .like(StorageConfig::getName, reqVO.getName())
                .eq(StorageConfig::getStorage, reqVO.getStorage())
                .orderByDesc(StorageConfig::getCreateTime)
        );
    }
}
