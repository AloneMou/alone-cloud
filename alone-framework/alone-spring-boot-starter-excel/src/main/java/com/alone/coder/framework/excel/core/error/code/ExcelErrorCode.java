package com.alone.coder.framework.excel.core.error.code;

import com.alone.coder.framework.common.exception.ErrorCode;

public interface ExcelErrorCode {

	// @ResponseExcel sheet 配置不合法
	ErrorCode EXCEL_SHEET_CONFIG_ERROR = new ErrorCode(501001, "excel sheet 配置不合法");

	// @ResponseExcel 返回值必须为List类型
	ErrorCode EXCEL_RETURN_TYPE_ERROR = new ErrorCode(501002, "返回值必须为List类型");

}
