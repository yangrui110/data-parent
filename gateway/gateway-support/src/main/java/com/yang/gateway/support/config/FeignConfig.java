package com.yang.gateway.support.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @project data-parent
 * @Date 2021/2/10
 * @Auth yangrui
 **/
@Configuration
@EnableFeignClients(basePackages = "com.yang.gateway.support.feign")
public class FeignConfig {
}
