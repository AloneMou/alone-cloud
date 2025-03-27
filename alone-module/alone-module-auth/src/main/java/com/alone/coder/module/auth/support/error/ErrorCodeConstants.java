package com.alone.coder.module.auth.support.error;


import com.alone.coder.framework.common.exception.ErrorCode;

/**
 * System 错误码枚举类
 * <p>
 * system 系统，使用 1-002-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== AUTH 模块 1-002-000-000 ==========
    ErrorCode AUTH_LOGIN_CAPTCHA_CODE_ERROR = new ErrorCode(1_002_000_004, "验证码不正确，原因：{}");
}
