
package com.alone.coder.framework.security.core.pojo;

import com.alone.coder.framework.security.core.enums.UserTypeEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.io.Serial;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lengleng
 */
public class LoginUser extends User implements OAuth2AuthenticatedPrincipal {

	@Serial
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	/**
	 * 扩展属性，方便存放oauth 上下文相关信息
	 */
	private final Map<String, Object> attributes = new HashMap<>();

	/**
	 * 用户ID
	 */
	@Getter
	private Long id;

	/**
	 * 用户类型
	 * <p>
	 * 关联 {@link UserTypeEnum}
	 */
	@Getter
	private Integer userType;

	/**
	 * Construct the <code>User</code> with the details required by
	 * {@link DaoAuthenticationProvider}.
	 * @param id 用户ID <code>DaoAuthenticationProvider</code>
	 * @param password the password that should be presented to the
	 * <code>DaoAuthenticationProvider</code>
	 * @param enabled set to <code>true</code> if the user is enabled
	 * @param accountNonExpired set to <code>true</code> if the account has not expired
	 * @param credentialsNonExpired set to <code>true</code> if the credentials have not
	 * expired
	 * @param accountNonLocked set to <code>true</code> if the account is not locked
	 * @param authorities the authorities that should be granted to the caller if they
	 * presented the correct username and password and the user is enabled. Not null.
	 * @throws IllegalArgumentException if a <code>null</code> value was passed either as
	 * a parameter or as an element in the <code>GrantedAuthority</code> collection
	 */
	@JsonCreator
	public LoginUser(@JsonProperty("id") Long id, @JsonProperty("username") String username,
			@JsonProperty("password") String password, @JsonProperty("enabled") boolean enabled,
			@JsonProperty("accountNonExpired") boolean accountNonExpired, @JsonProperty("userType") Integer userType,
			@JsonProperty("credentialsNonExpired") boolean credentialsNonExpired,
			@JsonProperty("accountNonLocked") boolean accountNonLocked,
			@JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
		this.userType = userType;

	}

	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	@Override
	public String getName() {
		return getUsername();
	}

}
