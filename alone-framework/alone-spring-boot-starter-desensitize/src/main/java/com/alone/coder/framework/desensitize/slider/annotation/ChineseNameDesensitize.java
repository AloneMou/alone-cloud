package com.alone.coder.framework.desensitize.slider.annotation;

import com.alone.coder.framework.desensitize.base.annotation.DesensitizeBy;
import com.alone.coder.framework.desensitize.slider.handler.ChineseNameDesensitization;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.*;

/**
 * 中文名
 *
 * @author AgoniMou
 */
@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@DesensitizeBy(handler = ChineseNameDesensitization.class)
public @interface ChineseNameDesensitize {

	/**
	 * 前缀保留长度
	 */
	int prefixKeep() default 1;

	/**
	 * 后缀保留长度
	 */
	int suffixKeep() default 0;

	/**
	 * 替换规则，中文名;比如：刘子豪脱敏之后为刘**
	 */
	String replacer() default "*";

}
