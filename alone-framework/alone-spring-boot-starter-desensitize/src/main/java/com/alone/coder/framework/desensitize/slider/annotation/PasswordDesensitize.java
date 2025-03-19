package com.alone.coder.framework.desensitize.slider.annotation;

import com.alone.coder.framework.desensitize.base.annotation.DesensitizeBy;
import com.alone.coder.framework.desensitize.slider.handler.PasswordDesensitization;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.*;

/**
 * 密码
 *
 * @author AgoniMou
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@DesensitizeBy(handler = PasswordDesensitization.class)
public @interface PasswordDesensitize {

    /**
     * 前缀保留长度
     */
    int prefixKeep() default 0;

    /**
     * 后缀保留长度
     */
    int suffixKeep() default 0;

    /**
     * 替换规则，密码;
     * <p>
     * 比如：123456 脱敏之后为 ******
     */
    String replacer() default "*";

}
