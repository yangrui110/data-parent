package com.yang.gateway.support.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * @project data-parent
 * @Date 2021/2/10
 * @Auth yangrui
 **/
@Component
@Order(1)
@Slf4j
public class PermissionFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String value = exchange.getRequest().getPath().value();
        log.info("路径：{}", value);
        if(value.equals("/system/login")) return chain.filter(exchange);
        List<String> accessToken = exchange.getRequest().getHeaders().get("Access-Token");
        if(accessToken==null){
            log.error("access_token为空，远程Ip：{}", exchange.getRequest().getRemoteAddress().getHostName());
            return Mono.empty();
        }
        log.info("获取到的Token:{}", accessToken);
        // 1、获取到请求的
        return chain.filter(exchange);
    }
}
