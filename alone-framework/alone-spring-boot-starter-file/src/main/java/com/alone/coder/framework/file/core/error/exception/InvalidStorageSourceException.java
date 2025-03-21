package com.alone.coder.framework.file.core.error.exception;

import com.alone.coder.framework.common.exception.enums.GlobalErrorCodeConstants;
import lombok.Getter;

import static com.alone.coder.framework.file.core.error.code.StorageErrorCode.STORAGE_SOURCE_NOT_FOUND;

/**
 * 无效的存储源异常
 *
 * @author zhaojun
 */
@Getter
public class InvalidStorageSourceException extends RuntimeException {

	/**
	 * 全局错误码
	 *
	 * @see GlobalErrorCodeConstants
	 */
	private final Integer code;

	/**
	 * 错误提示
	 */
	private final String message;

	/**
	 * 空构造方法，避免反序列化问题
	 */
	public InvalidStorageSourceException() {
		this.code = STORAGE_SOURCE_NOT_FOUND.getCode();
		this.message = STORAGE_SOURCE_NOT_FOUND.getMsg();
	}

	public InvalidStorageSourceException(String message) {
		this.code = STORAGE_SOURCE_NOT_FOUND.getCode();
		this.message = message;
	}

}