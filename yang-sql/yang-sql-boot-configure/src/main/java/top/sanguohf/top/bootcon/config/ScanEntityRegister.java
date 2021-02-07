package top.sanguohf.top.bootcon.config;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;
import top.sanguohf.top.bootcon.annotation.ScanEntity;
import top.sanguohf.top.bootcon.cache.EntityCache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@ConditionalOnMissingBean({ScanEntityConfigure.class})
public class ScanEntityRegister implements ImportBeanDefinitionRegistrar {

    public ScanEntityRegister() {
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        AnnotationAttributes mapperScanAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(ScanEntity.class.getName()));
        if (mapperScanAttrs != null) {
            this.registerBeanDefinitions(mapperScanAttrs, registry, generateBaseBeanName(importingClassMetadata, 0));
        }
    }

    void registerBeanDefinitions(AnnotationAttributes annotationAttributes, BeanDefinitionRegistry registry, String beanName) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ScanEntityConfigure.class);
        List<String> basePackages = new ArrayList();
        basePackages.addAll((Collection) Arrays.stream(annotationAttributes.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll((Collection)Arrays.stream(annotationAttributes.getStringArray("basePackages")).filter(StringUtils::hasText).collect(Collectors.toList()));

        builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));
        String[] strings = new String[basePackages.size()];
        basePackages.toArray(strings);
//        SqlConfigProperties.getInstance().setPackages(basePackages.toArray(strings));
        //注册缓存
        try {
            EntityCache.scanPackgeToCache(Arrays.asList(strings));
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("yang-sql： 实体类扫描异常");
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }

    private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata, int index) {
        return importingClassMetadata.getClassName() + "#" + ScanEntityRegister.class.getSimpleName() + "#" + index;
    }
}
