package com.alone.coder.framework.desensitize.regex.handler;


import com.alone.coder.framework.desensitize.regex.annotation.EmailDesensitize;

/**
 * {@link EmailDesensitize} 的脱敏处理器
 *
 * @author AgoniMou
 */
public class EmailDesensitizationHandler extends AbstractRegexDesensitizationHandler<EmailDesensitize> {

    @Override
    String getRegex(EmailDesensitize annotation) {
        return annotation.regex();
    }

    @Override
    String getReplacer(EmailDesensitize annotation) {
        return annotation.replacer();
    }

}
