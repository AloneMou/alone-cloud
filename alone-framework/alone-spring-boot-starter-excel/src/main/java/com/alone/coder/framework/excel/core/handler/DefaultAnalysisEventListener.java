package com.alone.coder.framework.excel.core.handler;

import cn.idev.excel.context.AnalysisContext;
import com.alone.coder.framework.common.util.validation.ValidationUtils;
import com.alone.coder.framework.excel.core.annotation.ExcelLine;
import com.alone.coder.framework.excel.core.error.code.ExcelErrorMessage;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 默认的 AnalysisEventListener
 *
 * @author lengleng
 * @author L.cm
 * @date 2021/4/16
 */
@Slf4j
public class DefaultAnalysisEventListener extends ListAnalysisEventListener<Object> {

	// 数据列表
	private final List<Object> list = new ArrayList<>();

	// 错误信息
	private final List<ExcelErrorMessage> errorMessageList = new ArrayList<>();

	// 行号
	private Long lineNum = 1L;

	@Override
	public void invoke(Object o, AnalysisContext analysisContext) {
		lineNum++;
		Set<ConstraintViolation<Object>> violations = ValidationUtils.validate(o);
		if (!violations.isEmpty()) {
			Set<String> messageSet = violations.stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.toSet());
			errorMessageList.add(new ExcelErrorMessage(lineNum, messageSet));
		}
		else {
			Field[] fields = o.getClass().getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(ExcelLine.class) && field.getType() == Long.class) {
					try {
						field.setAccessible(true);
						field.set(o, lineNum);
					}
					catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			list.add(o);
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
		log.debug("Excel read analysed");
	}

	@Override
	public List<Object> getList() {
		return list;
	}

	@Override
	public List<ExcelErrorMessage> getErrors() {
		return errorMessageList;
	}

}
