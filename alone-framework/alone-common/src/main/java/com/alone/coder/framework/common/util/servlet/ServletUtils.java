package com.alone.coder.framework.common.util.servlet;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.alone.coder.framework.common.util.json.JsonUtils;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

/**
 * 客户端工具类
 *
 * @author 芋道源码
 */
@Slf4j
@UtilityClass
public class ServletUtils {
    private final String BASIC_ = "Basic ";

    /**
     * 返回 JSON 字符串
     *
     * @param response 响应
     * @param object   对象，会序列化成 JSON 字符串
     */
    @SuppressWarnings("deprecation") // 必须使用 APPLICATION_JSON_UTF8_VALUE，否则会乱码
    public static void writeJSON(HttpServletResponse response, Object object) {
        String content = JsonUtils.toJsonString(object);
        JakartaServletUtil.write(response, content, MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    /**
     * @param request 请求
     * @return ua
     */
    public static String getUserAgent(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        return ua != null ? ua : "";
    }

    /**
     * 获得请求
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public static String getUserAgent() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return getUserAgent(request);
    }

    public static String getClientIP() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return JakartaServletUtil.getClientIP(request);
    }

    public static boolean isJsonRequest(ServletRequest request) {
        return StrUtil.startWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE);
    }

    public static String getBody(HttpServletRequest request) {
        // 只有在 json 请求在读取，因为只有 CacheRequestBodyFilter 才会进行缓存，支持重复读取
        if (isJsonRequest(request)) {
            return JakartaServletUtil.getBody(request);
        }
        return null;
    }

    public static byte[] getBodyBytes(HttpServletRequest request) {
        // 只有在 json 请求在读取，因为只有 CacheRequestBodyFilter 才会进行缓存，支持重复读取
        if (isJsonRequest(request)) {
            return JakartaServletUtil.getBodyBytes(request);
        }
        return null;
    }

    public static String getClientIP(HttpServletRequest request) {
        return JakartaServletUtil.getClientIP(request);
    }

    public static Map<String, String> getParamMap(HttpServletRequest request) {
        return JakartaServletUtil.getParamMap(request);
    }


    /**
     * 解析 client id
     *
     * @param header
     * @param defVal
     * @return 如果解析失败返回默认值
     */
    public String extractClientId(String header, final String defVal) {

        if (header == null || !header.startsWith(BASIC_)) {
            log.debug("请求头中client信息为空: {}", header);
            return defVal;
        }
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            log.debug("Failed to decode basic authentication token: {}", header);
            return defVal;
        }

        String token = new String(decoded, StandardCharsets.UTF_8);

        int delim = token.indexOf(":");

        if (delim == -1) {
            log.debug("Invalid basic authentication token: {}", header);
            return defVal;
        }
        return token.substring(0, delim);
    }

    /**
     * 从请求头中解析 client id
     *
     * @param header
     * @return
     */
    public Optional<String> extractClientId(String header) {
        return Optional.ofNullable(extractClientId(header, null));
    }
}
