package com.alone.coder.framework.security.core.service;

public interface PermissionService {

	/**
	 * 判断是否有权限
	 * @param permission 权限
	 * @return 是否
	 */
	boolean hasPermission(String permission);

	/**
	 * 判断是否有权限，任一一个即可
	 * @param permissions 权限
	 * @return 是否
	 */
	boolean hasAnyPermissions(String... permissions);

}
