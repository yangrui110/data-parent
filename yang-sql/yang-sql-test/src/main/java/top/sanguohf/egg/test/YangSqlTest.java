package top.sanguohf.egg.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import top.sanguohf.top.bootcon.annotation.ScanEntity;
import top.sanguohf.top.bootcon.service.CommonService;
import top.sanguohf.top.bootcon.service.impl.CommonServiceImpl;

@EnableTransactionManagement
@ScanEntity({"top.sanguohf.egg.test.entity","top.sanguohf.egg.configure.entity","top.sanguohf.egg.test.view"})
@SpringBootApplication
public class YangSqlTest {
    public static void main(String[] args) {
        SpringApplication.run(YangSqlTest.class);
    }

    @Bean
    public CommonService commonService(JdbcTemplate jdbcTemplate){
        CommonServiceImpl commonService = new CommonServiceImpl(jdbcTemplate);
        return commonService;
    }
}
