package com.alone.coder.framework.common.enums;

import cn.hutool.core.util.ObjUtil;
import com.alone.coder.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.util.Arrays.stream;

/**
 * 通用状态枚举
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum implements ArrayValuable<Integer> {

	ENABLE(0, "开启"), DISABLE(1, "关闭");

	public static final Integer[] ARRAYS = stream(values()).map(CommonStatusEnum::getStatus).toArray(Integer[]::new);

	/**
	 * 状态值
	 */
	private final Integer status;

	/**
	 * 状态名
	 */
	private final String name;

	@Override
	public Integer[] array() {
		return ARRAYS;
	}

	public static boolean isEnable(Integer status) {
		return ObjUtil.equal(ENABLE.status, status);
	}

	public static boolean isDisable(Integer status) {
		return ObjUtil.equal(DISABLE.status, status);
	}

}
