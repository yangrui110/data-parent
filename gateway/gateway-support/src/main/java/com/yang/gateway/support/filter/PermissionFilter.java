package com.yang.gateway.support.filter;

import com.alibaba.fastjson.JSONObject;
import com.yang.system.client.entity.Api;
import com.yang.system.client.entity.Role;
import com.yang.system.client.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @project data-parent
 * @Date 2021/2/18
 * @Auth yangrui
 **/
@Component
@Order(8)
@Slf4j
public class PermissionFilter implements GlobalFilter {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1、获取到当前用户的api权限
        try {
            String value = exchange.getRequest().getPath().value();
            if(value.equals("/system/login")) return chain.filter(exchange);
            String token = exchange.getRequest().getHeaders().getFirst("Access-Token");
            String s = redisTemplate.opsForValue().get(token);
            UserVo userVo = JSONObject.parseObject(s).toJavaObject(UserVo.class);
            // 2 如果是超级角色所有者，直接放行
            List<Role> roles = userVo.getRoles();
            for(Role role: roles){
                if(role.getRoleCode().equals("SUPER_ROLE")){
                    return chain.filter(exchange);
                }
            }
            List<Api> apisByRedis = userVo.getApis();
            // 2、检测是否具有当前接口的权限
            if (apisByRedis == null) return writeNoProccess(exchange);
            for (Api api : apisByRedis) {
                String apiPath = api.getApiPath();
                if (apiPath.equals(value)) {
                    return chain.filter(exchange);
                }
            }
        }catch (Exception e){
            return writeNoProccess(exchange);
        }
        return writeNoProccess(exchange);
    }

    private Mono<Void> writeNoProccess(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        JSONObject message = new JSONObject();
        message.put("code", 5001);
        message.put("message", "您没有当前接口的权限");
        byte[] bits = message.toJSONString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.OK);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }
}
