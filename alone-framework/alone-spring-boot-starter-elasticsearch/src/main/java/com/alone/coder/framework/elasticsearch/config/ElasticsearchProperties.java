package com.alone.coder.framework.elasticsearch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * elasticsearch 配置属性
 */
@Data
@ConfigurationProperties(prefix = ElasticsearchProperties.PREFIX)
public class ElasticsearchProperties {

    static final String PREFIX = "elasticsearch";

    /**
     * 是否为多集群模式
     */
    private boolean isMultiCluster = false;


    /**
     * 单集群配置，multiCluster=false 时生效
     */
    private Cluster standalone = new Cluster();

    /**
     * 多集群配置，每个集群名对应一个集群配置
     */
    private Map<String, Cluster> clusters;

    /**
     * 集群配置
     */
    @Data
    public static class Cluster {

        /**
         * 集群中的多个节点列表
         */
        private List<Node> nodes;

        /**
         * 账号
         */
        private String username;

        /**
         * 密码
         */
        private String password;

        /**
         * 连接超时时间（毫秒）
         */
        private int connectTimeoutMillis = 1000;

        /**
         * socket超时时间（毫秒）
         */
        private int socketTimeoutMillis = 30000;

        /**
         * 最大连接数
         */
        private int maxConnectTotal = 100;

        /**
         * 最大路由连接数
         */
        private int maxConnectPerRoute = 100;
    }

    /**
     * 节点配置
     */
    @Data
    public static class Node {

        /**
         * 地址
         */
        private String host;

        /**
         * 端口
         */
        private int port = 9200;

        /**
         * 协议
         */
        private String scheme = "http";
    }

}
