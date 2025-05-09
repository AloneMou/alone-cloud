package com.alone.coder.framework.desensitize.slider.annotation;

import com.alone.coder.framework.desensitize.base.annotation.DesensitizeBy;
import com.alone.coder.framework.desensitize.slider.handler.CarLicenseDesensitization;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.*;

/**
 * 车牌号
 *
 * @author AgoniMou
 */
@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@DesensitizeBy(handler = CarLicenseDesensitization.class)
public @interface CarLicenseDesensitize {

	/**
	 * 前缀保留长度
	 */
	int prefixKeep() default 3;

	/**
	 * 后缀保留长度
	 */
	int suffixKeep() default 1;

	/**
	 * 替换规则，车牌号;比如：粤A66666 脱敏之后为粤A6***6
	 */
	String replacer() default "*";

}
