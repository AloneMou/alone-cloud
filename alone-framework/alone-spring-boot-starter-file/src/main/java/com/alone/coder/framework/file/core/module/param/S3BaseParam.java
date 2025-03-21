package com.alone.coder.framework.file.core.module.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

/**
 * @author AgoniMou
 */
@Data
public class S3BaseParam implements IStorageParam {

	/**
	 * AccessKey
	 */
	@NotNull(message = "accessKey 不能为空")
	private String accessKey;

	/**
	 * SecretKey
	 */
	@NotNull(message = "secretKey 不能为空")
	private String secretKey;

	/**
	 * 区域
	 */
	@NotNull(message = "endpoint 不能为空")
	private String endPoint;

	/**
	 * 存储空间名称
	 */
	@NotNull(message = "bucket 不能为空")
	private String bucketName;

	/**
	 * Bucket 域名 / CDN 加速域名
	 */
	@URL(message = "domain 必须是 URL 格式")
	private String domain;

	/**
	 * 基路径
	 * <p>
	 * 基路径表示读取的根文件夹，不填写表示允许读取所有。如： '/'，'/文件夹1'
	 * </p>
	 */
	private String basePath;

	/**
	 * 是否是私有空间
	 * <p>
	 * 私有空间会生成带签名的下载链接
	 * </p>
	 */
	private boolean isPrivate;

	/**
	 * 下载签名有效期
	 * <p>
	 * 当为私有空间时, 用于下载签名的有效期, 单位为秒, 如不配置则默认为 1800 秒.
	 * </p>
	 */
	private Integer tokenTime;

	/**
	 * 是否自动配置 CORS 跨域设置
	 * <p>
	 * 如不配置跨域设置，可能会无法导致无法上传，或上传后看不到文件（某些 S3 存储无需配置此选项，如 Cloudflare R2、Oracle 对象存储）
	 * </p>
	 */
	private boolean autoConfigCors;

}
