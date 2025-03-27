package com.alone.coder.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lengleng
 * <p>
 * 密码是否加密传输
 */
@Getter
@AllArgsConstructor
public enum EncFlagTypeEnum {

	/**
	 * 是
	 */
	YES("1", "是"),

	/**
	 * 否
	 */
	NO("0", "否");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

}
