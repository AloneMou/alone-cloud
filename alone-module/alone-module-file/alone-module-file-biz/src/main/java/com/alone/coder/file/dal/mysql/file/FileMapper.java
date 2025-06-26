package com.alone.coder.file.dal.mysql.file;

import com.alone.coder.file.controller.admin.file.vo.file.FilePageReqVO;
import com.alone.coder.file.dal.dataobject.file.FileDO;
import com.alone.coder.framework.common.pojo.PageResult;
import com.alone.coder.framework.mybatis.core.mapper.BaseMapperX;
import com.alone.coder.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件操作 Mapper
 *
 * @author AgoniMou
 */
@Mapper
public interface FileMapper extends BaseMapperX<FileDO> {

    default PageResult<FileDO> selectPage(FilePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FileDO>()
                .likeIfPresent(FileDO::getPath, reqVO.getPath())
                .likeIfPresent(FileDO::getType, reqVO.getType())
                .betweenIfPresent(FileDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(FileDO::getId));
    }

}
