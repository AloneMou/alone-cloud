package com.alone.coder.framework.desensitize.slider.handler;

import com.alone.coder.framework.desensitize.slider.annotation.BankCardDesensitize;

/**
 * {@link BankCardDesensitize} 的脱敏处理器
 *
 * @author AgoniMou
 */
public class BankCardDesensitization extends AbstractSliderDesensitizationHandler<BankCardDesensitize> {

	@Override
	Integer getPrefixKeep(BankCardDesensitize annotation) {
		return annotation.prefixKeep();
	}

	@Override
	Integer getSuffixKeep(BankCardDesensitize annotation) {
		return annotation.suffixKeep();
	}

	@Override
	String getReplacer(BankCardDesensitize annotation) {
		return annotation.replacer();
	}

}
