package com.alone.coder.module.auth.support.filter;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alone.coder.framework.common.constant.SecurityConstants;
import com.alone.coder.framework.common.enums.EncFlagTypeEnum;
import com.alone.coder.framework.common.util.servlet.ServletUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Map;

import static com.alone.coder.module.auth.support.filter.ValidateCodeFilter.CLIENT_FLAG;

/**
 * @author lengleng
 * @date 2019 /2/1 密码解密工具类
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordDecoderFilter extends OncePerRequestFilter {

    private final AuthSecurityConfigProperties authSecurityConfigProperties;

    private final StringRedisTemplate redisTemplate;

    private static final String PASSWORD = "password";

    private static final String KEY_ALGORITHM = "AES";

    static {
        // 关闭hutool 强制关闭Bouncy Castle库的依赖
        SecureUtil.disableBouncyCastle();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // 不是登录请求，直接向下执行
        if (!StrUtil.containsAnyIgnoreCase(request.getRequestURI(), SecurityConstants.OAUTH_TOKEN_URL)) {
            chain.doFilter(request, response);
            return;
        }

        // 2. 刷新token类型，直接向下执行
        String grantType = request.getParameter("grant_type");
        if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)) {
            chain.doFilter(request, response);
            return;
        }

        // 3. 判断客户端是否需要解密，明文传输直接向下执行
        if (!isEncClient(request)) {
            chain.doFilter(request, response);
            return;
        }

        // 将请求流转换为可多次读取的请求流
        CacheRequestBodyWrapper requestWrapper = new CacheRequestBodyWrapper(request);
        Map<String, String[]> parameterMap = requestWrapper.getParameterMap();

        // 构建前端对应解密AES 因子
        AES aes = new AES(Mode.CFB, Padding.NoPadding,
                new SecretKeySpec(authSecurityConfigProperties.getEncodeKey().getBytes(), KEY_ALGORITHM),
                new IvParameterSpec(authSecurityConfigProperties.getEncodeKey().getBytes()));

        parameterMap.forEach((k, v) -> {
            String[] values = parameterMap.get(k);
            if (!PASSWORD.equals(k) || ArrayUtil.isEmpty(values)) {
                return;
            }
            // 解密密码
            String decryptPassword = aes.decryptStr(values[0]);
            parameterMap.put(k, new String[]{decryptPassword});
        });
        chain.doFilter(requestWrapper, response);
    }

    /**
     * 根据请求的clientId 查询客户端配置是否是加密传输
     *
     * @param request 请求上下文
     * @return true 加密传输 、 false 原文传输
     */
    private boolean isEncClient(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String clientId = ServletUtils.extractClientId(header).orElse(null);
        // 获取租户拼接区分租户的key
        String key = "%s:%s".formatted(CLIENT_FLAG, clientId);
        String val = redisTemplate.opsForValue().get(key);
        // 当配置不存在时，默认需要解密
        if (val == null) {
            return true;
        }
        JSONObject information = JSONUtil.parseObj(val);
        return !StrUtil.equals(EncFlagTypeEnum.NO.getType(), information.getStr("ENC_FLAG"));
    }

}
