package com.yang.gateway.support.filter;

import com.alibaba.fastjson.JSONObject;
import com.yang.gateway.support.feign.SystemFeignApi;
import com.yang.system.client.entity.WhitePath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @project data-parent
 * @Date 2021/2/10
 * @Auth yangrui
 **/
@Component
@Order(10)
@Slf4j
public class WhitePathFilter implements GlobalFilter {
    @Autowired
    SystemFeignApi systemFeignApi;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String value = exchange.getRequest().getPath().value();
        log.info("路径：{}", value);
        boolean whitePath = isWhitePath(value);
        if(whitePath){
            log.info("{} 是白名单", value);
            return chain.filter(exchange);
        }
//        if(value.equals("/system/login")) return chain.filter(exchange);
        List<String> accessToken = exchange.getRequest().getHeaders().get("Access-Token");
        if(accessToken==null){
            log.error("access_token为空，远程Ip：{}", exchange.getRequest().getRemoteAddress().getHostName());
            return writeNeedLogin(exchange);
        }
        log.info("获取到的Token:{}", accessToken);
        // 1、获取到请求的
        return chain.filter(exchange);
    }

    private Mono<Void> writeNeedLogin(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        JSONObject message = new JSONObject();
        message.put("code", 5000);
        message.put("message", "鉴权失败");
        byte[] bits = message.toJSONString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    private boolean isWhitePath(String path){
        List<WhitePath> whitePathVos = systemFeignApi.listWhitePath();
        for(WhitePath whitePathVo: whitePathVos){
            // 1、如果是以**结尾,就排除掉所有以其开头的
            String whitePath = whitePathVo.getWhitePath();
            if(whitePath.endsWith("/**")){
                String substring = whitePath.substring(0, whitePath.length() - 3);
                if(path.startsWith(substring)){
                    return true;
                }
            }else if(path.equals(whitePath)) {
                return true;
            }else {
                // 如果path是带参数的，那么就去掉参数
                int indexOf = path.indexOf("?");
                if(indexOf!=-1){
                    String substring = path.substring(0, indexOf);
                    if(substring.equals(whitePath)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
