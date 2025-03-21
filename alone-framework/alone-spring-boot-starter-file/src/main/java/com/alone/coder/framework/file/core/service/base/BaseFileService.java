package com.alone.coder.framework.file.core.service.base;

import com.alone.coder.framework.file.core.module.enums.StorageTypeEnum;

/**
 * 基础文件服务接口，定义了了一些通用方法定义
 *
 * @author AgoniMou
 */
public interface BaseFileService {

	/**
	 * 获取存储源类型
	 * @return 存储源类型
	 */
	StorageTypeEnum getStorageTypeEnum();

	/**
	 * 上传文件
	 * @param content 文件流
	 * @param path 相对路径
	 * @return 完整路径，即 HTTP 访问地址
	 * @throws Exception 上传文件时，抛出 Exception 异常
	 */
	String upload(byte[] content, String path, String type) throws Exception;

	/**
	 * 获取文件下载地址
	 * @param pathAndName 文件路径及文件名称
	 * @return 文件下载地址
	 */
	String getDownloadUrl(String pathAndName);

	/**
	 * 创建新文件夹
	 * @param path 文件夹路径
	 * @param name 文件夹名称
	 * @return 是否创建成功
	 */
	boolean newFolder(String path, String name);

	/**
	 * 删除文件
	 * @param path 相对路径
	 * @return 是否删除成功
	 * @throws Exception 删除文件时，抛出 Exception 异常
	 */
	boolean deleteFile(String path) throws Exception;

	/**
	 * 获得文件的内容
	 * @param path 相对路径
	 * @return 文件的内容
	 */
	byte[] getContent(String path) throws Exception;

	/**
	 * 获取存储ID
	 * @return 存储ID
	 */
	String getStorageId();

}
