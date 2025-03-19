package com.alone.coder.framework.desensitize.slider.annotation;

import com.alone.coder.framework.desensitize.base.annotation.DesensitizeBy;
import com.alone.coder.framework.desensitize.slider.handler.BankCardDesensitization;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.*;

/**
 * 银行卡号
 *
 * @author AgoniMou
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@DesensitizeBy(handler = BankCardDesensitization.class)
public @interface BankCardDesensitize {

    /**
     * 前缀保留长度
     */
    int prefixKeep() default 6;

    /**
     * 后缀保留长度
     */
    int suffixKeep() default 2;

    /**
     * 替换规则，银行卡号; 比如：9988002866797031 脱敏之后为 998800********31
     */
    String replacer() default "*";

}
