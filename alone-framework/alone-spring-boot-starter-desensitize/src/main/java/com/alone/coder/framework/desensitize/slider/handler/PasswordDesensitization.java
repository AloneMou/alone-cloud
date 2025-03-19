package com.alone.coder.framework.desensitize.slider.handler;


import com.alone.coder.framework.desensitize.slider.annotation.PasswordDesensitize;

/**
 * {@link PasswordDesensitize} 的码脱敏处理器
 *
 * @author AgoniMou
 */
public class PasswordDesensitization extends AbstractSliderDesensitizationHandler<PasswordDesensitize> {
    @Override
    Integer getPrefixKeep(PasswordDesensitize annotation) {
        return annotation.prefixKeep();
    }

    @Override
    Integer getSuffixKeep(PasswordDesensitize annotation) {
        return annotation.suffixKeep();
    }

    @Override
    String getReplacer(PasswordDesensitize annotation) {
        return annotation.replacer();
    }
}
