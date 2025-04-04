package com.alone.coder.framework.desensitize.regex.handler;

import com.alone.coder.framework.desensitize.regex.annotation.RegexDesensitize;

/**
 * {@link RegexDesensitize} 的正则脱敏处理器
 *
 * @author AgoniMou
 */
public class DefaultRegexDesensitizationHandler extends AbstractRegexDesensitizationHandler<RegexDesensitize> {

	@Override
	String getRegex(RegexDesensitize annotation) {
		return annotation.regex();
	}

	@Override
	String getReplacer(RegexDesensitize annotation) {
		return annotation.replacer();
	}

}
