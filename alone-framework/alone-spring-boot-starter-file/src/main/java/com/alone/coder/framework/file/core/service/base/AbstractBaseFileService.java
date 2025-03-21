package com.alone.coder.framework.file.core.service.base;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alone.coder.framework.file.core.module.param.IStorageParam;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AgoniMou
 */
@Getter
@Slf4j
public abstract class AbstractBaseFileService<P extends IStorageParam> implements BaseFileService {

	/**
	 * 存储源初始化配置
	 */
	public P param;

	/**
	 * 是否初始化成功
	 */
	protected boolean isInitialized = false;

	/**
	 * 存储源 ID
	 */
	public String storageId;

	public AbstractBaseFileService(String storageId, P param) {
		if (ObjUtil.hasEmpty(storageId, param)) {
			throw new IllegalStateException("初始化参数不能为空");
		}
		this.storageId = storageId;
		this.param = param;
	}

	/**
	 * 刷新存储配置
	 * @param param 参数
	 */
	public final void refresh(P param) {
		// 判断是否更新
		if (param.equals(this.param)) {
			return;
		}
		log.info("[refresh][配置({})发生变化，重新初始化]", param);
		this.param = param;
		// 初始化
		this.init();
	}

	/**
	 * 初始化存储源, 在调用前要设置存储的 {@link #storageId} 属性. 和 {@link #param} 属性.
	 */
	public abstract void init();

	/**
	 * 获取存储简单信息
	 * @return {@link String }
	 */
	String getStorageSimpleInfo() {
		return StrUtil.format("存储源 [id={},  type: {}]", storageId, getStorageTypeEnum().getDescription());
	}

}
