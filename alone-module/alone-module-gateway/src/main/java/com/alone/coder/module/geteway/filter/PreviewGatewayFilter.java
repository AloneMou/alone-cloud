package com.alone.coder.module.geteway.filter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 */
@Slf4j
@Component
public class PreviewGatewayFilter extends AbstractGatewayFilterFactory {

    private static final String TOKEN = "token";

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // GET，直接向下执行
            if (StrUtil.equalsIgnoreCase(request.getMethod().name(), HttpMethod.GET.name())
                    || StrUtil.containsIgnoreCase(request.getURI().getPath(), TOKEN)) {
                return chain.filter(exchange);
            }

            log.warn("演示环境不能操作-> {},{}", request.getMethod().name(), request.getURI().getPath());
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.LOCKED);
            return response.setComplete();
        };
    }

}
