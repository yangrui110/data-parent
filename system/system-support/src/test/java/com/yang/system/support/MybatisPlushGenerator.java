package com.yang.system.support;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * @project data-parent
 * @Date 2021/2/9
 * @Auth yangrui
 **/
public class MybatisPlushGenerator {

    public static void main(String[] args) {
        String entityPath = "/system/system-client/src/main/java/com/yang/system/client/entity/";
        String entityPackageInfo = "com.yang.system.client.entity";
        String daoPackageInfo = "com.yang.system.support.dao";
        String servicePackageInfo = "com.yang.system.support.service";
        String serviceImplPackageInfo = "com.yang.system.support.service.impl";
        String controllerPackageInfo = "com.yang.system.support.controller";
        String mapperPath = "/system/system-support/src/main/resources/mapper/";

        AutoGenerator generator = new AutoGenerator();

        // 输出文件的配置
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        globalConfig.setOutputDir(projectPath + "/system/system-support/src/main/java");
        globalConfig.setAuthor("yangrui");
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        globalConfig.setServiceName("%sService");
        globalConfig.setControllerName("%sController");
        globalConfig.setMapperName("%sDao");
        globalConfig.setOpen(false);
        globalConfig.setActiveRecord(false);
        globalConfig.setFileOverride(true);
        globalConfig.setSwagger2(true);
        generator.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://212.64.50.170:3306/springsys?serverTimezone=UTC");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setPassword("yr134167");
        dataSourceConfig.setUsername("root");
        generator.setDataSource(dataSourceConfig);

        // 包的配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("");
        packageConfig.setEntity(entityPackageInfo);
        packageConfig.setMapper(daoPackageInfo);
        packageConfig.setService(servicePackageInfo);
        packageConfig.setServiceImpl(serviceImplPackageInfo);
        packageConfig.setController(controllerPackageInfo);
        generator.setPackageInfo(packageConfig);

        //配置策略
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setInclude("white_path");
        strategyConfig.setEntityLombokModel(true);
        strategyConfig.setEntityBooleanColumnRemoveIsPrefix(true);
        generator.setStrategy(strategyConfig);

        // 设置模板
        generator.setTemplateEngine(new FreemarkerTemplateEngine());
        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + mapperPath + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        focList.add(new FileOutConfig("/templates/entity.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + entityPath + tableInfo.getEntityName() + StringPool.DOT_JAVA;
            }
        });
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        cfg.setFileOutConfigList(focList);
        generator.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null); // 控制默认不生成xml文件
        templateConfig.setEntity(null); // 控制默认不生成entity文件
        /*templateConfig.setMapper(null);
        templateConfig.setController(null);
        templateConfig.setService(null);
        templateConfig.setServiceImpl(null);*/
        generator.setTemplate(templateConfig);
        // 执行反向代码生成
        generator.execute();
    }
}
