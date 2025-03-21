package com.alone.coder.framework.desensitize.regex.annotation;

import com.alone.coder.framework.desensitize.base.annotation.DesensitizeBy;
import com.alone.coder.framework.desensitize.regex.handler.EmailDesensitizationHandler;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.*;

/**
 * 邮箱脱敏注解
 *
 * @author AgoniMou
 */
@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@DesensitizeBy(handler = EmailDesensitizationHandler.class)
public @interface EmailDesensitize {

	/**
	 * 匹配的正则表达式
	 */
	String regex() default "(^.)[^@]*(@.*$)";

	/**
	 * 替换规则，邮箱;
	 * <p>
	 * 比如：example@gmail.com 脱敏之后为 e****@gmail.com
	 */
	String replacer() default "$1****$2";

}
