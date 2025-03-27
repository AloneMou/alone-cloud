package com.alone.coder.module.auth.support.core;

import com.alone.coder.framework.security.core.handler.FormAuthenticationFailureHandler;
import com.alone.coder.module.auth.support.handler.SsoLogoutSuccessHandler;
import com.alone.coder.module.auth.support.handler.TenantSavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * @author lengleng
 * @data 2022-06-04
 * <p>
 * 基于授权码模式 统一认证登录 spring security & sas 都可以使用 所以抽取成 HttpConfigurer
 */
public final class FormIdentityLoginConfigurer
        extends AbstractHttpConfigurer<FormIdentityLoginConfigurer, HttpSecurity> {

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.formLogin(formLogin -> {
                    formLogin.loginPage("/token/login");
                    formLogin.loginProcessingUrl("/oauth2/form");
                    formLogin.failureHandler(new FormAuthenticationFailureHandler());
                    formLogin.successHandler(new TenantSavedRequestAwareAuthenticationSuccessHandler());
                })
                .logout(logout -> logout.logoutUrl("/oauth2/logout")
                        .logoutSuccessHandler(new SsoLogoutSuccessHandler())
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)) // SSO登出成功处理

                .csrf(AbstractHttpConfigurer::disable);
    }

}
