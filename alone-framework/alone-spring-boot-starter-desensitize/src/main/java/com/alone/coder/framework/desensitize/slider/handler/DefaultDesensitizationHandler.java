package com.alone.coder.framework.desensitize.slider.handler;

import com.alone.coder.framework.desensitize.slider.annotation.SliderDesensitize;

/**
 * {@link SliderDesensitize} 的脱敏处理器
 *
 * @author AgoniMou
 */
public class DefaultDesensitizationHandler extends AbstractSliderDesensitizationHandler<SliderDesensitize> {

	@Override
	Integer getPrefixKeep(SliderDesensitize annotation) {
		return annotation.prefixKeep();
	}

	@Override
	Integer getSuffixKeep(SliderDesensitize annotation) {
		return annotation.suffixKeep();
	}

	@Override
	String getReplacer(SliderDesensitize annotation) {
		return annotation.replacer();
	}

}
