package com.alone.coder.framework.desensitize.slider.handler;

import com.alone.coder.framework.desensitize.slider.annotation.MobileDesensitize;

/**
 * {@link MobileDesensitize} 的脱敏处理器
 *
 * @author AgoniMou
 */
public class MobileDesensitization extends AbstractSliderDesensitizationHandler<MobileDesensitize> {

	@Override
	Integer getPrefixKeep(MobileDesensitize annotation) {
		return annotation.prefixKeep();
	}

	@Override
	Integer getSuffixKeep(MobileDesensitize annotation) {
		return annotation.suffixKeep();
	}

	@Override
	String getReplacer(MobileDesensitize annotation) {
		return annotation.replacer();
	}

}
