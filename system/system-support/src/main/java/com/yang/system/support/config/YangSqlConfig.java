package com.yang.system.support.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import top.sanguohf.top.bootcon.annotation.ScanEntity;
import top.sanguohf.top.bootcon.service.CommonService;
import top.sanguohf.top.bootcon.service.impl.CommonServiceImpl;

/**
 * @project data-parent
 * @Date 2021/2/7
 * @Auth yangrui
 **/
@Configuration
@ScanEntity(basePackages = "com.yang.system.client.entity")
public class YangSqlConfig {

    @Bean
    public CommonService commonService(JdbcTemplate jdbcTemplate){
        CommonServiceImpl commonService = new CommonServiceImpl(jdbcTemplate);
        return commonService;
    }

}
