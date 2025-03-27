
package com.alone.coder.framework.security.core.service;

import com.alone.coder.framework.common.pojo.CommonResult;
import com.alone.coder.framework.common.util.res.RetOps;
import com.alone.coder.framework.security.core.enums.UserTypeEnum;
import com.alone.coder.framework.security.core.pojo.LoginUser;
import com.alone.coder.module.system.api.user.vo.UserInfo;
import com.alone.coder.module.system.api.user.AdminUserApi;
import com.alone.coder.module.system.enums.ApiConstants;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Objects;
import java.util.Optional;

/**
 * 用户详细信息 default
 *
 * @author lengleng
 */
@Slf4j
@Primary
@RequiredArgsConstructor
public class AloneDefaultUserDetailsServiceImpl implements AuthUserDetailsService {

	private final AdminUserApi adminUserApi;

	private final CacheManager cacheManager;

	/**
	 * 用户密码登录
	 * @param username 用户名
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	@SneakyThrows
	public UserDetails loadUserByUsername(String username) {
		Cache cache = cacheManager.getCache(ApiConstants.USER_DETAILS);
		if (Objects.nonNull(cache) && Objects.nonNull(cache.get(username))) {
			return getUserDetails(Optional.ofNullable(cache.get(username, UserInfo.class)));
		}
		CommonResult<UserInfo> result = adminUserApi.info(username);
		Optional<UserInfo> userInfoOptional = RetOps.of(result).getData();
		userInfoOptional.ifPresent(userInfo -> Objects.requireNonNull(cache).put(username, userInfo));
		return getUserDetails(userInfoOptional);
	}

	/**
	 * 通过用户实体查询
	 * @param loginUser user
	 * @return
	 */
	@Override
	public UserDetails loadUserByUser(LoginUser loginUser) {
		// 避免 C端用户通过接口调用B端接口的安全问题
		if (UserTypeEnum.ADMIN.getValue().equals(loginUser.getUserType())) {
			return loadUserByUsername(loginUser.getUsername());
		}
		return null;
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}

}
