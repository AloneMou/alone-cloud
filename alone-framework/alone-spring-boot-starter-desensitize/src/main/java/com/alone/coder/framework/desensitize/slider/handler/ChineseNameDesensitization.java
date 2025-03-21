package com.alone.coder.framework.desensitize.slider.handler;

import com.alone.coder.framework.desensitize.slider.annotation.ChineseNameDesensitize;

/**
 * {@link ChineseNameDesensitize} 的脱敏处理器
 *
 * @author AgoniMou
 */
public class ChineseNameDesensitization extends AbstractSliderDesensitizationHandler<ChineseNameDesensitize> {

	@Override
	Integer getPrefixKeep(ChineseNameDesensitize annotation) {
		return annotation.prefixKeep();
	}

	@Override
	Integer getSuffixKeep(ChineseNameDesensitize annotation) {
		return annotation.suffixKeep();
	}

	@Override
	String getReplacer(ChineseNameDesensitize annotation) {
		return annotation.replacer();
	}

}
