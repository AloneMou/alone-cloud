package com.alone.coder.framework.file.core.module.param;

import lombok.Data;
import lombok.Getter;

/**
 * 又拍云初始化参数
 *
 * @author zhaojun
 */
@Data
public class UpYunParam implements IStorageParam {

	/**
	 * 存储空间名称
	 */
	private String bucketName;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 下载域名
	 * <p>
	 * 填写您在又拍云绑定的域名.
	 * </p>
	 */
	private String domain;

	/**
	 * 基路径
	 * <p>
	 * 基路径表示读取的根文件夹，不填写表示允许读取所有。如： '/'，'/文件夹1'.
	 * </p>
	 */
	private String basePath;

	/**
	 * Token
	 * <p>
	 * 可在又拍云后台开启 "访问控制" -> "Token 防盗链"，控制资源内容的访问时限，即时间戳防盗链。
	 * </p>
	 * <p>
	 * <a href="https://help.upyun.com/knowledge-base/cdn-token-limite/">官方配置文档 </a>
	 * </p>
	 */
	private String token;

	/**
	 * Token 有效期
	 * <p>
	 * Token 有效期，单位为秒。
	 * </p>
	 * <p>
	 * 默认值为 1800 秒，即 30 分钟。
	 * </p>
	 */
	private int tokenTime;

}