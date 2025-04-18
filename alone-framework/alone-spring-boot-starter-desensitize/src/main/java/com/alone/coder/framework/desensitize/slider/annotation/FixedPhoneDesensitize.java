package com.alone.coder.framework.desensitize.slider.annotation;

import com.alone.coder.framework.desensitize.base.annotation.DesensitizeBy;
import com.alone.coder.framework.desensitize.slider.handler.FixedPhoneDesensitization;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.*;

/**
 * 固定电话
 *
 * @author AgoniMou
 */
@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@DesensitizeBy(handler = FixedPhoneDesensitization.class)
public @interface FixedPhoneDesensitize {

	/**
	 * 前缀保留长度
	 */
	int prefixKeep() default 4;

	/**
	 * 后缀保留长度
	 */
	int suffixKeep() default 2;

	/**
	 * 替换规则，固定电话;比如：01086551122 脱敏之后为 0108*****22
	 */
	String replacer() default "*";

}
