package com.alone.coder.framework.common.constant;

public interface SecurityConstants {

    /**
     * 内部
     */
    String FROM_IN = "Y";

    /**
     * 标志
     */
    String FROM = "from";

    /**
     * {bcrypt} 加密的特征码
     */
    String BCRYPT = "{bcrypt}";

    /**
     * {noop} 加密的特征码
     */
    String NOOP = "{noop}";


    /**
     * 客户端编号
     */
    String CLIENT_ID = "client_id";

    /**
     * 用户名
     */
    String DETAILS_USERNAME = "username";

    /**
     * 用户ID
     */
    String DETAILS_USER_ID = "user_id";

    /**
     * 客户端允许同时在线数量
     */
    String ONLINE_QUANTITY = "online_quantity";


    /**
     * 客户端模式
     */
    String CLIENT_CREDENTIALS = "client_credentials";

    /**
     * OAUTH URL
     */
    String OAUTH_TOKEN_URL = "/oauth2/token";


    /**
     * 刷新
     */
    String REFRESH_TOKEN = "refresh_token";
}
