package com.alone.coder.framework.feign.config;


import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@AutoConfiguration
@EnableConfigurationProperties(RestTemplateProperties.class)
public class RestTemplateAutoConfigure {

    /**
     * 使用连接池的 httpclient
     */
    @Bean
    public HttpClient httpClient(RestTemplateProperties restTemplateProperties) {
        PoolingHttpClientConnectionManager connectionManager =new PoolingHttpClientConnectionManager();
        // 最大链接数
        connectionManager.setMaxTotal(restTemplateProperties.getMaxTotal());
        // 同路由并发数20
        connectionManager.setDefaultMaxPerRoute(restTemplateProperties.getMaxPerRoute());

        ConnectionConfig connectConfig = ConnectionConfig.custom()
                // 读超时
                .setSocketTimeout(Timeout.ofMicroseconds(restTemplateProperties.getReadTimeout()))
                // 链接超时
                .setConnectTimeout(Timeout.ofMicroseconds(restTemplateProperties.getConnectTimeout()))
                .build();
        connectionManager.setDefaultConnectionConfig(connectConfig);

        RequestConfig requestConfig = RequestConfig.custom()
                // 链接不够用的等待时间
                .setConnectionRequestTimeout(Timeout.ofMicroseconds(restTemplateProperties.getReadTimeout()))
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
    }

    /**
     * httpclient 实现的ClientHttpRequestFactory
     */
    @Bean
    public ClientHttpRequestFactory httpRequestFactory(HttpClient httpClient) {
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory httpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(httpRequestFactory);
        return restTemplate;
    }

}
