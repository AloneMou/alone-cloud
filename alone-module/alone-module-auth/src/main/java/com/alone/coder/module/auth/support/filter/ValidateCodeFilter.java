package com.alone.coder.module.auth.support.filter;

/**
 * 登录前处理器
 *
 * @author lengleng
 * @date 2024/4/3
 */

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.shaded.com.google.common.annotations.VisibleForTesting;
import com.alone.coder.framework.common.constant.SecurityConstants;
import com.alone.coder.framework.common.exception.ServiceException;
import com.alone.coder.framework.common.util.servlet.ServletUtils;
import com.alone.coder.framework.common.util.spring.SpringUtils;
import com.alone.coder.framework.common.util.validation.ValidationUtils;
import com.alone.coder.framework.security.core.enums.GrantTypeEnums;
import com.xingyuv.captcha.model.common.CaptchaTypeEnum;
import com.xingyuv.captcha.model.common.ResponseModel;
import com.xingyuv.captcha.model.vo.CaptchaVO;
import com.xingyuv.captcha.service.CaptchaService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.alone.coder.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.alone.coder.module.auth.support.error.ErrorCodeConstants.AUTH_LOGIN_CAPTCHA_CODE_ERROR;
import static com.alone.coder.module.auth.support.sms.OAuth2ResourceOwnerSmsAuthenticationConverter.SMS_PARAMETER_NAME;

/**
 * @author lbw
 * @date 2024-01-06
 * <p>
 * 登录前置处理器： 前端密码传输密文解密，验证码处理
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {

    private final StringRedisTemplate redisTemplate;

    /**
     * 客户端配置缓存
     */
    public static final String CLIENT_FLAG = "client_config_flag";
    /**
     * 验证码的开关，默认为 true
     */
    @Value("${captcha.enable:true}")
    @Setter // 为了单测：开启或者关闭验证码
    private Boolean captchaEnable;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestUrl = request.getServletPath();

        // 不是登录URL 请求直接跳过
        if (!SecurityConstants.OAUTH_TOKEN_URL.equals(requestUrl)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 如果登录URL 但是刷新token的请求，直接向下执行
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (StrUtil.containsAny(grantType, SecurityConstants.REFRESH_TOKEN)) {
            filterChain.doFilter(request, response);
            return;
        }

        // mobile模式, 如果请求不包含mobile 参数直接
        String mobile = request.getParameter(SMS_PARAMETER_NAME);
        if (StrUtil.equals(GrantTypeEnums.SMS.getType(), grantType) && StrUtil.isBlank(mobile)) {
            throw new OAuth2AuthenticationException("授权码模式，mobile参数不能为空");
        }

        // mobile模式, 社交登录模式不校验验证码直接跳过
        if (StrUtil.equals(GrantTypeEnums.SMS.getType(), grantType) && !StrUtil.contains(mobile, "SMS")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 判断客户端是否跳过检验
        if (!isCheckCaptchaClient(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 校验验证码 1. 客户端开启验证码 2. 短信模式
        try {
            validateCaptcha();
            filterChain.doFilter(request, response);
        } catch (ServiceException validateCodeException) {
            throw new OAuth2AuthenticationException(validateCodeException.getMessage());
        }
    }

    @VisibleForTesting
    void validateCaptcha() {
        // 如果验证码关闭，则不进行校验
        if (!captchaEnable) {
            return;
        }
        String captchaVerification = ServletUtils.getRequest().getParameter("captchaVerification");
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(captchaVerification);
        ResponseModel response = SpringUtils.getBean(CaptchaService.class).verification(captchaVO);
        // 校验验证码
        if (!response.isSuccess()) {
            throw exception(AUTH_LOGIN_CAPTCHA_CODE_ERROR, response.getRepMsg());
        }
    }

    /**
     * 是否需要校验客户端，根据client 查询客户端配置
     *
     * @param request 请求
     * @return true 需要校验， false 不需要校验
     */
    private boolean isCheckCaptchaClient(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String clientId = ServletUtils.extractClientId(header).orElse(null);
        // 获取租户拼接区分租户的key
//        String tenantId = request.getHeader(CommonConstants.TENANT_ID);
        String key = "%s:%s".formatted(CLIENT_FLAG, clientId);
        String val = redisTemplate.opsForValue().get(key);
        // 当配置不存在时，不用校验
        if (val == null) {
            return false;
        }
        JSONObject information = JSONUtil.parseObj(val);
        return !StrUtil.equals("OFF", information.getStr("CAPTCHA_FLAG"));

    }
}
