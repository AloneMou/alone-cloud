package com.alone.coder.framework.desensitize.base.handler;

import java.lang.annotation.Annotation;

/**
 * 脱敏处理器接口
 *
 * @author AgoniMou
 */
public interface DesensitizationHandler<T extends Annotation> {

	/**
	 * 脱敏
	 * @param origin 原始字符串
	 * @param annotation 注解信息
	 * @return 脱敏后的字符串
	 */
	String desensitize(String origin, T annotation);

}
