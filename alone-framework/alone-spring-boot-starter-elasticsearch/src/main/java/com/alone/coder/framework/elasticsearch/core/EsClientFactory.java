package com.alone.coder.framework.elasticsearch.core;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.alone.coder.framework.elasticsearch.config.ElasticsearchProperties;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EsClientFactory {

    private final Map<String, ElasticsearchClient> clientMap = new ConcurrentHashMap<>();

    public EsClientFactory(Map<String, ElasticsearchProperties.Cluster> clusters) {
        clusters.forEach((name, cluster) -> {
            ElasticsearchClient client = buildClient(cluster);
            clientMap.put(name, client);
        });
    }

    private ElasticsearchClient buildClient(ElasticsearchProperties.Cluster cluster) {
        // 这里可以复用自动装配的逻辑，或者提取公共方法
        // 简单示例：重复创建逻辑
        List<HttpHost> httpHosts = cluster.getNodes().stream()
                .map(node -> new HttpHost(node.getHost(), node.getPort(), node.getScheme()))
                .toList();

        RestClientBuilder builder = RestClient.builder(httpHosts.toArray(new HttpHost[0]));

        if (cluster.getUsername() != null && !cluster.getUsername().isEmpty()) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(cluster.getUsername(), cluster.getPassword()));

            builder.setHttpClientConfigCallback(httpClientBuilder ->
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        }

        builder.setRequestConfigCallback(requestConfig ->
                requestConfig
                        .setConnectTimeout(cluster.getConnectTimeoutMillis())
                        .setSocketTimeout(cluster.getSocketTimeoutMillis()));

        builder.setHttpClientConfigCallback(httpClientBuilder ->
                httpClientBuilder
                        .setMaxConnTotal(cluster.getMaxConnectTotal())
                        .setMaxConnPerRoute(cluster.getMaxConnectPerRoute()));

        RestClient restClient = builder.build();
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    public ElasticsearchClient getClient(String clusterName) {
        return clientMap.get(clusterName);
    }

    // 关闭客户端资源
    public void close() throws Exception {
        for (ElasticsearchClient client : clientMap.values()) {
            if (client != null) {
                // 关闭底层RestClient
                client._transport().close();
            }
        }
    }
}
