package com.alone.coder.framework.elasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.alone.coder.framework.elasticsearch.core.EsClientFactory;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
@RequiredArgsConstructor
public class ElasticsearchAutoConfiguration {

    private final ElasticsearchProperties elasticsearchProperties;

    /**
     * 单集群模式，注入 ElasticsearchClient Bean
     */
    @Bean
    @ConditionalOnProperty(prefix = "es", name = "multi-cluster", havingValue = "false", matchIfMissing = true)
    public ElasticsearchClient elasticsearchClient() {
        ElasticsearchProperties.Cluster cluster = elasticsearchProperties.getStandalone();
        return createClient(cluster);
    }

    /**
     * 多集群客户端工厂
     */
    @Bean
    @ConditionalOnProperty(prefix = "es", name = "multi-cluster", havingValue = "true")
    public EsClientFactory esClientFactory() {
        return new EsClientFactory(elasticsearchProperties.getClusters());
    }

    /**
     * 创建 ElasticsearchClient 实例
     */
    private ElasticsearchClient createClient(ElasticsearchProperties.Cluster cluster) {
        List<HttpHost> httpHosts = cluster.getNodes().stream()
                .map(node -> new HttpHost(node.getHost(), node.getPort(), node.getScheme()))
                .toList();

        RestClientBuilder builder = RestClient.builder(httpHosts.toArray(new HttpHost[0]));

        // 设置认证信息（如果有）
        if (cluster.getUsername() != null && !cluster.getUsername().isEmpty()) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(cluster.getUsername(), cluster.getPassword()));

            builder.setHttpClientConfigCallback(httpClientBuilder ->
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        }

        // 设置连接超时等参数
        builder.setRequestConfigCallback(requestConfig ->
                requestConfig
                        .setConnectTimeout(cluster.getConnectTimeoutMillis())
                        .setSocketTimeout(cluster.getSocketTimeoutMillis()));

        builder.setHttpClientConfigCallback(httpClientBuilder ->
                httpClientBuilder
                        .setMaxConnTotal(cluster.getMaxConnectTotal())
                        .setMaxConnPerRoute(cluster.getMaxConnectPerRoute()));

        RestClient restClient = builder.build();
        // 创建传输层，使用Jackson作为JSON解析器
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        // 创建高级客户端
        return new ElasticsearchClient(transport);
    }
}
