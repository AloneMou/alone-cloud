package com.alone.coder.file.convert.file;


import com.alone.coder.file.controller.admin.file.vo.config.StorageConfigCreateReqVO;
import com.alone.coder.file.controller.admin.file.vo.config.StorageConfigRespVO;
import com.alone.coder.file.controller.admin.file.vo.config.StorageConfigUpdateReqVO;
import com.alone.coder.file.dal.dataobject.file.StorageConfig;
import com.alone.coder.framework.common.pojo.PageResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author AgoniMou
 */
@Mapper
public interface StorageConfigConvert {

    StorageConfigConvert INSTANCE = Mappers.getMapper(StorageConfigConvert.class);

    @Mapping(target = "config", ignore = true)
    StorageConfig convert(StorageConfigCreateReqVO bean);

    @Mapping(target = "config", ignore = true)
    StorageConfig convert(StorageConfigUpdateReqVO bean);

    StorageConfigRespVO convert(StorageConfig bean);

    List<StorageConfigRespVO> convertList(List<StorageConfig> list);

    PageResult<StorageConfigRespVO> convertPage(PageResult<StorageConfig> pageResult);
}
