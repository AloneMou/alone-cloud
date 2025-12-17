package com.alone.coder.framework.file.core.module.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AgoniMou
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MinIOParam extends S3BaseParam {

    /**
     * 服务地址
     * <p>
     * 为 minio 的服务地址，非 web 访问地址，需包含协议，如 <a href="http://ip:9000">http://ip:9000</a>
     * </p>
     */
    private String endPoint;

    /**
     * 服务地址协议
     */
    private String endPointScheme;

    /**
     * 地域
     */
    private String region = "minio";

    /**
     * 访问域名
     * <p>为 minio 的服务地址，非 web 访问地址，一般为 <a href="http://ip:9000">http://ip:9000</a></p>
     */
    private String domain;

}
