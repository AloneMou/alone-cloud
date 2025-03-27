package com.alone.coder.framework.security.core.service;

import cn.hutool.core.util.ArrayUtil;
import com.alone.coder.framework.common.constant.SecurityConstants;
import com.alone.coder.framework.security.core.enums.GrantTypeEnums;
import com.alone.coder.framework.security.core.pojo.LoginUser;
import com.alone.coder.module.system.api.user.vo.UserInfo;
import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

public interface AuthUserDetailsService extends UserDetailsService, Ordered {

	/**
	 * Notfound 用户错误代码
	 */
	String NOTFOUND_USER_ERROR_CODE = "UserDetailsService.notFound";

	/**
	 * 是否支持此客户端校验
	 * @param clientId 请求客户端
	 * @param grantType 授权类型
	 * @return true/false
	 */
	default boolean support(String clientId, GrantTypeEnums grantType) {
		return true;
	}

	/**
	 * 排序值 默认取最大的
	 * @return 排序值
	 */
	default int getOrder() {
		return 0;
	}

	/**
	 * 获取用户详细信息
	 * @param userInfoOptional 用户信息：可选
	 * @return {@link UserDetails }
	 */
	default UserDetails getUserDetails(Optional<UserInfo> userInfoOptional) {
		// @formatter:off
        return  userInfoOptional
                .map(this::convertUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        // @formatter:on
	}

	/**
	 * UserInfo 转 UserDetails
	 * @param info
	 * @return 返回UserDetails对象
	 */
	default UserDetails convertUserDetails(UserInfo info) {
		Set<String> dbAuthsSet = new HashSet<>();
		if (ArrayUtil.isNotEmpty(info.getPermissions())) {
			// 获取角色
			// Arrays.stream(info.getRoles()).forEach(roleId ->
			// dbAuthsSet.add(SecurityConstants.ROLE + roleId));
			// 获取资源
			dbAuthsSet.addAll(info.getPermissions());
		}
		Collection<? extends GrantedAuthority> authorities = AuthorityUtils
			.createAuthorityList(dbAuthsSet.toArray(new String[0]));
		// 构造security用户
		return new LoginUser(info.getId(), info.getUsername(), SecurityConstants.BCRYPT + info.getPassword(), true,
				true, info.getUserType(), false// 密码过期判断
				, 1 == info.getStatus(), authorities);
	}

	/**
	 * 通过用户实体查询
	 * @param loginUser user
	 * @return UserDetails
	 */
	default UserDetails loadUserByUser(LoginUser loginUser) {
		return this.loadUserByUsername(loginUser.getUsername());
	}

}
