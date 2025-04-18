package com.alone.coder.framework.excel.core.head;

/**
 * Excel头生成器，用于自定义生成头部信息
 */
public interface HeadGenerator {

	/**
	 * <p>
	 * 自定义头部信息
	 * </p>
	 * 实现类根据数据的class信息，定制Excel头<br/>
	 * 具体方法使用参考：<a href=
	 * "https://idev.cn/fastexcel/zh-CN">https://idev.cn/fastexcel/zh-CN</a>
	 * @param clazz 当前sheet的数据类型
	 * @return List<List < String>> Head头信息
	 */
	HeadMeta head(Class<?> clazz);

}
