package com.yang.system.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import top.sanguohf.top.bootcon.annotation.ScanEntity;

/**
 * @project data-parent
 * @Date 2021/2/7
 * @Auth yangrui
 **/
@SpringBootApplication
@ScanEntity(basePackages = "com.yang.system.client.entity")
@RefreshScope
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class,args);
    }

}
