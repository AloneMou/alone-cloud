package com.alone.coder.framework.xxl.job.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * on-hook-boot
 *
 * @author mjw
 * @since 2025/5/22 23:41
 */
@ConfigurationProperties(prefix = "xxl.job")
@Validated
@Data
public class XxlJobProperties {

    /**
     * 管理配置
     */
    @NotNull(message = "XxlJob 管理配置不能为空")
    private XxlJobAdmin admin;

    /**
     * 执行器配置
     */
    @NotNull(message = "XxlJob 执行器配置不能为空")
    private XxlJobExecutor executor;


    @Data
    public static class XxlJobAdmin {

        /**
         * 管理地址
         */
        private String address;

        /**
         * 访问令牌
         */
        private String accessToken;

        /**
         * 超时时间
         */
        @NotNull(message = "XxlJob 管理配置超时时间不能为空")
        private Integer timeout = 5000;
    }

    @Data
    public static class XxlJobExecutor {

        /**
         * 执行器名称
         */
        @NotBlank(message = "XxlJob 执行器名称不能为空")
        private String appname;

        /**
         * 执行器地址
         */
        private String address;

        /**
         * 执行器IP
         */
        private String ip;

        /**
         * 执行器端口
         */
        @NotNull(message = "XxlJob 执行器端口不能为空")
        private Integer port = 7070;

        /**
         * 执行器日志路径
         */
        @NotBlank(message = "XxlJob 执行器日志路径不能为空")
        private String logPath = "./logs/xxl-job";

        /**
         * 执行器日志文件保存天数
         */
        @NotNull(message = "XxlJob 执行器日志文件保存天数不能为空")
        private Integer logRetentionDays = 7;
    }

}
