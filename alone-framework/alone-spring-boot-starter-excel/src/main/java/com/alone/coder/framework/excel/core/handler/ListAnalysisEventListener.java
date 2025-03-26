package com.alone.coder.framework.excel.core.handler;

import cn.idev.excel.event.AnalysisEventListener;
import com.alone.coder.framework.excel.core.error.code.ExcelErrorMessage;

import java.util.List;

/**
 * list analysis EventListener
 *
 * @author L.cm
 */
public abstract class ListAnalysisEventListener<T> extends AnalysisEventListener<T> {

	/**
	 * 获取 excel 解析的对象列表
	 * @return 集合
	 */
	public abstract List<T> getList();

	/**
	 * 获取异常校验结果
	 * @return 集合
	 */
	public abstract List<ExcelErrorMessage> getErrors();

}
