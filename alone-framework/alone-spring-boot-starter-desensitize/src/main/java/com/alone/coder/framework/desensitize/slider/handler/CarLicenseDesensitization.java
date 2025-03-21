package com.alone.coder.framework.desensitize.slider.handler;

import com.alone.coder.framework.desensitize.slider.annotation.CarLicenseDesensitize;

/**
 * {@link CarLicenseDesensitize} 的脱敏处理器
 *
 * @author AgoniMou
 */
public class CarLicenseDesensitization extends AbstractSliderDesensitizationHandler<CarLicenseDesensitize> {

	@Override
	Integer getPrefixKeep(CarLicenseDesensitize annotation) {
		return annotation.prefixKeep();
	}

	@Override
	Integer getSuffixKeep(CarLicenseDesensitize annotation) {
		return annotation.suffixKeep();
	}

	@Override
	String getReplacer(CarLicenseDesensitize annotation) {
		return annotation.replacer();
	}

}
