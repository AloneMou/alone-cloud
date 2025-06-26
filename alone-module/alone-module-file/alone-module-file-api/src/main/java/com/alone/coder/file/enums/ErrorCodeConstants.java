package com.alone.coder.file.enums;


import com.alone.coder.framework.common.exception.ErrorCode;

/**
 * Infra 错误码枚举类
 *
 * @author AgoniMou
 */
public interface ErrorCodeConstants {

    // ========== 文件配置 1001006000 ==========
    ErrorCode STORAGE_CONFIG_NOT_EXISTS = new ErrorCode(1001006000, "文件配置不存在");
    ErrorCode STORAGE_CONFIG_DELETE_FAIL_MASTER = new ErrorCode(1001006001, "该文件配置不允许删除，原因：它是主配置，删除会导致无法上传文件");


    // ========= 文件相关 1-001-003-000 =================
    ErrorCode FILE_PATH_EXISTS = new ErrorCode(1_001_003_000, "文件路径已存在");
    ErrorCode FILE_NOT_EXISTS = new ErrorCode(1_001_003_001, "文件不存在");
    ErrorCode FILE_IS_EMPTY = new ErrorCode(1_001_003_002, "文件为空");

}
