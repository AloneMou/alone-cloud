package com.alone.coder.framework.file.core.error.exception;

import com.alone.coder.framework.common.exception.ErrorCode;
import com.alone.coder.framework.common.exception.enums.GlobalErrorCodeConstants;

public class InitializeStorageSourceException extends RuntimeException {

    /**
     * 全局错误码
     *
     * @see GlobalErrorCodeConstants
     */
    private Integer code;
    /**
     * 错误提示
     */
    private String message;

    /**
     * 空构造方法，避免反序列化问题
     */
    public InitializeStorageSourceException() {
    }

    public InitializeStorageSourceException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMsg();
    }

    public InitializeStorageSourceException(ErrorCode errorCode, String message) {
        this.code = errorCode.getCode();
        this.message = message;
    }

    public InitializeStorageSourceException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public InitializeStorageSourceException setCode(Integer code) {
        this.code = code;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public InitializeStorageSourceException setMessage(String message) {
        this.message = message;
        return this;
    }

}
