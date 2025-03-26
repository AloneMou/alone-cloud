package com.alone.coder.framework.excel.core.error.code;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * 校验错误信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelErrorMessage {

	/**
	 * 行号
	 */
	private Long lineNum;

	/**
	 * 错误信息
	 */
	private Set<String> errors = new HashSet<>();

	public ExcelErrorMessage(Set<String> errors) {
		this.errors = errors;
	}

	public ExcelErrorMessage(String error) {
		HashSet<String> objects = new HashSet<>();
		objects.add(error);
		this.errors = objects;
	}

}
