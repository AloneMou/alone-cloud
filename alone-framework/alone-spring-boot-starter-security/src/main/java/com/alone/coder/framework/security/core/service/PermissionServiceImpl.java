package com.alone.coder.framework.security.core.service;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

public class PermissionServiceImpl implements PermissionService {

	@Override
	public boolean hasPermission(String permission) {
		return hasAnyPermissions(permission);
	}

	@Override
	public boolean hasAnyPermissions(String... permissions) {
		if (ArrayUtil.isEmpty(permissions)) {
			return false;
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			return false;
		}
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		return authorities.stream()
			.map(GrantedAuthority::getAuthority)
			.filter(StringUtils::hasText)
			.anyMatch(x -> PatternMatchUtils.simpleMatch(permissions, x));
	}

}
