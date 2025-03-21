package com.alone.coder.framework.file.core.module.enums;

import cn.hutool.core.util.ArrayUtil;
import com.alone.coder.framework.file.core.module.param.*;
import com.alone.coder.framework.file.core.service.base.AbstractBaseFileService;
import com.alone.coder.framework.file.core.service.impl.*;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储源类型枚举
 *
 * @author AgoniMou
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StorageTypeEnum implements IEnum {

	/**
	 * 当前系统支持的所有存储源类型
	 */
	LOCAL("local", "本地存储", LocalServiceImpl.class, LocalParam.class),
	/**
	 * 阿里云 OSS
	 */
	ALIYUN("aliyun", "阿里云 OSS", AliyunServiceImpl.class, AliYunParam.class),
	/**
	 * 腾讯云 COS
	 */
	TENCENT("tencent", "腾讯云 COS", TencentServiceImpl.class, TencentParam.class),
	/**
	 * 华为云 OBS
	 */
	HUAWEI("huawei", "华为云 OBS", HuaweiServiceImpl.class, HuaweiParam.class),
	/**
	 * MINIO
	 */
	MINIO("minio", "MINIO", MinIOServiceImpl.class, MinIOParam.class),
	/**
	 * S3通用协议
	 */
	S3("s3", "S3通用协议", S3ServiceImpl.class, S3Param.class),
	/**
	 * 七牛云 KODO
	 */
	QINIU("qiniu", "七牛云 KODO", QiniuServiceImpl.class, QiniuParam.class),
	/**
	 * 又拍云
	 */
	UPYUN("upyun", "又拍云", UpYunServiceImpl.class, UpYunParam.class),

	;

	private static final Map<String, StorageTypeEnum> ENUM_MAP = new HashMap<>();

	static {
		for (StorageTypeEnum type : StorageTypeEnum.values()) {
			ENUM_MAP.put(type.getKey(), type);
		}
	}

	public static StorageTypeEnum getByStorage(String key) {
		return ArrayUtil.firstMatch(o -> o.getKey().equals(key), values());
	}

	/**
	 * 存储源类型枚举 Key (aliyun)
	 */
	@EnumValue
	private final String key;

	/**
	 * 存储源类型枚举描述 (阿里云 OSS)
	 */
	private final String description;

	/**
	 * 配置类
	 */
	private final Class<? extends AbstractBaseFileService> clientClass;

	/**
	 * 配置类
	 */
	private final Class<? extends IStorageParam> configClass;

	StorageTypeEnum(String key, String description, Class<? extends AbstractBaseFileService> clientClass,
			Class<? extends IStorageParam> configClass) {
		this.key = key;
		this.description = description;
		this.clientClass = clientClass;
		this.configClass = configClass;
	}

	@JsonIgnore
	public String getValue() {
		return key;
	}

}
