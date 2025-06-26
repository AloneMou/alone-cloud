package com.alone.coder.file.convert.file;

import com.alone.coder.file.controller.admin.file.vo.file.FileRespVO;
import com.alone.coder.file.dal.dataobject.file.FileDO;
import com.alone.coder.framework.common.pojo.PageResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author AgoniMou
 * @date 2024/1/10
 */
@Mapper
public interface FileConvert {

    FileConvert INSTANCE = Mappers.getMapper(FileConvert.class);

    PageResult<FileRespVO> convertPage(PageResult<FileDO> pageResult);
}
