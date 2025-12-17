package com.alone.coder.framework.file.core.error.code;

import com.alone.coder.framework.common.exception.ErrorCode;

public interface StorageErrorCode {

    ErrorCode STORAGE_SOURCE_NOT_FOUND = new ErrorCode(40102, "无效的或初始化失败的存储源");

    ErrorCode STORAGE_SOURCE_FORBIDDEN = new ErrorCode(40103, "无权访问存储源");

    ErrorCode STORAGE_SOURCE_FILE_FORBIDDEN = new ErrorCode(40104, "无权访问该目录");

    ErrorCode STORAGE_SOURCE_ILLEGAL_OPERATION = new ErrorCode(40105, "非法操作");

    // -------------- 服务端处理错误 --------------

    // 初始化相关错误
    ErrorCode STORAGE_SOURCE_INIT_FAIL = new ErrorCode(50100, "初始化存储源失败");

    ErrorCode STORAGE_SOURCE_INIT_STORAGE_CONFIG_FAIL = new ErrorCode(50101, "初始化存储源参数失败");

    ErrorCode STORAGE_SOURCE_INIT_STORAGE_PARAM_FIELD_FAIL = new ErrorCode(50102, "填充存储源字段失败");

    // 文件操作相关错误
    ErrorCode STORAGE_SOURCE_FILE_NEW_FOLDER_FAIL = new ErrorCode(50201, "新建文件夹失败");

    ErrorCode STORAGE_SOURCE_FILE_DELETE_FAIL = new ErrorCode(50202, "删除失败");

    ErrorCode STORAGE_SOURCE_FILE_RENAME_FAIL = new ErrorCode(50203, "重命名失败");

    ErrorCode STORAGE_SOURCE_FILE_GET_UPLOAD_FAIL = new ErrorCode(50204, "获取上传链接失败");

    ErrorCode STORAGE_SOURCE_FILE_PROXY_UPLOAD_FAIL = new ErrorCode(50205, "文件上传失败");

    ErrorCode STORAGE_SOURCE_FILE_PROXY_DOWNLOAD_FAIL = new ErrorCode(50206, "文件下载失败");

    ErrorCode STORAGE_SOURCE_FILE_GET_ITEM_FAIL = new ErrorCode(50207, "文件不存在或请求异常");

    ErrorCode STORAGE_SOURCE_FILE_DISABLE_PROXY_DOWNLOAD = new ErrorCode(50208, "非法操作, 当前文件不支持此类下载方式");

    //=============================S3异常错误码============================

    //当使用域名访问时, 域名风格只能使用路径模式, 请修改存储配置中的域名风格选项.
    ErrorCode STORAGE_SOURCE_S3_DOMAIN_STYLE_ERROR = new ErrorCode(50209, "域名风格只能使用路径模式");

}
