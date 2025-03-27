package com.alone.coder.module.auth.support.core;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alone.coder.framework.common.util.servlet.ServletUtils;
import com.alone.coder.framework.security.core.enums.GrantTypeEnums;
import com.alone.coder.framework.security.core.service.AuthUserDetailsService;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

/**
 * @author lengleng
 * @date 2022-06-04
 */
public class PigxDaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    /**
     * The plaintext password used to perform PasswordEncoder#matches(CharSequence,
     * String)} on when the user is not found to avoid SEC-2056.
     */
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    private final static BasicAuthenticationConverter basicConvert = new BasicAuthenticationConverter();

    private PasswordEncoder passwordEncoder;

    /**
     * The password used to perform {@link PasswordEncoder#matches(CharSequence, String)}
     * on when the user is not found to avoid SEC-2056. This is necessary, because some
     * {@link PasswordEncoder} implementations will short circuit if the password is not
     * in a valid format.
     */
    private volatile String userNotFoundEncodedPassword;

    @Setter
    private UserDetailsService userDetailsService;

    @Setter
    private UserDetailsPasswordService userDetailsPasswordService;

    public PigxDaoAuthenticationProvider() {
        setMessageSource(SpringUtil.getBean("securityMessageSource"));
        setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        // 短信 模式不用校验密码
        String grantType = ServletUtils.getRequest().getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (StrUtil.equals(GrantTypeEnums.SMS.getType(), grantType)) {
            return;
        }

        if (authentication.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            this.logger.debug("Failed to authenticate since password does not match stored value");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }

    @SneakyThrows
    @Override
    protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
        prepareTimingAttackProtection();
        String grantType = ServletUtils.getRequest().getParameter(OAuth2ParameterNames.GRANT_TYPE);
        String clientId = ServletUtils.getRequest().getParameter(OAuth2ParameterNames.CLIENT_ID);

        if (StrUtil.isBlank(clientId)) {
            clientId = basicConvert.convert(ServletUtils.getRequest()).getName();
        }

        Map<String, AuthUserDetailsService> userDetailsServiceMap = SpringUtil.getBeansOfType(AuthUserDetailsService.class);

        String finalClientId = clientId;
        Optional<AuthUserDetailsService> optional = userDetailsServiceMap.values()
                .stream()
                .filter(service -> service.support(finalClientId, GrantTypeEnums.create(grantType)))
                .max(Comparator.comparingInt(Ordered::getOrder));

        if (optional.isEmpty()) {
            throw new InternalAuthenticationServiceException("UserDetailsService error , not register");
        }

        try {
            UserDetails loadedUser = optional.get().loadUserByUsername(username);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        } catch (UsernameNotFoundException ex) {
            mitigateAgainstTimingAttack(authentication);
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
                                                         UserDetails user) {
        boolean upgradeEncoding = this.userDetailsPasswordService != null
                && this.passwordEncoder.upgradeEncoding(user.getPassword());
        if (upgradeEncoding) {
            String presentedPassword = authentication.getCredentials().toString();
            String newPassword = this.passwordEncoder.encode(presentedPassword);
            user = this.userDetailsPasswordService.updatePassword(user, newPassword);
        }
        return super.createSuccessAuthentication(principal, authentication, user);
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
        }
    }

    private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }
    }

    /**
     * Sets the PasswordEncoder instance to be used to encode and validate passwords. If
     * not set, the password will be compared using
     * {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}
     *
     * @param passwordEncoder must be an instance of one of the {@code PasswordEncoder}
     *                        types.
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }

    protected UserDetailsService getUserDetailsService() {
        return this.userDetailsService;
    }

}
