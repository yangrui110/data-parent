package top.sanguohf.top.bootcon.cache;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @project yang-sql
 * @Date 2020/8/5
 * @Auth 杨瑞
 **/
public class EntityCache {

    private static ConcurrentHashMap<String,Class> cache = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String,Element> cacheXml = new ConcurrentHashMap<>();

    /** @param classes
     **/
    public static void addClass(Class classes) {
        // TODO: implement
        if(classes==null) throw new RuntimeException("实体类不能为空");
        String key = classes.getSimpleName();
        if(cache.get(key) !=null) throw new RuntimeException("实体类："+key+"名称重复定义");
        cache.put(key,classes);
    }

    /** @param classPath
     **/
    public static void addClass(String classPath) throws ClassNotFoundException {
        // TODO: implement
        if(classPath==null||"".equals(classPath)) throw new RuntimeException("类路径不能为空");
        Class<?> forName = Class.forName(classPath);
        String key = classPath.substring(classPath.lastIndexOf(".")+1);
        if(cache.get(key) !=null) throw new RuntimeException("实体类："+key+"重复定义(不可同时定义两个名称相同的类名)");
        cache.put(key,forName);
    }

    public static void scanPackgeToCache(List<String> basePackages) throws ClassNotFoundException, IOException, DocumentException {
        for(String basePackage: basePackages) {
            String resourcePath = ClassUtils.convertClassNameToResourcePath(new StandardEnvironment().resolveRequiredPlaceholders(basePackage));
            //Class<?> user = ClassUtils.forName("User", ClassUtils.getDefaultClassLoader());
            String packageSearchPath = "classpath*:" + resourcePath + '/' + "**/**";
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(packageSearchPath);
            for (Resource resource : resources) {
                if(resource.getURL().toString().endsWith(".class")){
                    String all = resource.getURL().toString().replaceAll("/", ".");
                    int start = all.indexOf(basePackage);
                    int end = all.lastIndexOf(".class");
                    String substring = all.substring(start, end);
                    addClass(substring);
                }else if(resource.getURL().toString().endsWith(".xml")){
                    InputStream inputStream = resource.getInputStream();
                    SAXReader reader = new SAXReader();
                    Document read = reader.read(inputStream);
                    Element rootElement = read.getRootElement();
                    List<Element> sqls = rootElement.elements("sql");
                    for (Element element: sqls){
                        String id = element.attributeValue("id");
                        if(cacheXml.get(id) !=null) throw new RuntimeException("主键："+id+"重复定义");
                        cacheXml.put(id,element);
                    }
                }
            }
        }
    }

    /** @param className
     *  */
    public static Class getClass(String className) {
        // TODO: implement
        Class aClass=cache.get(className);
        return aClass;
    }

    public static Element getXml(String id) {
        // TODO: implement
        Element element=cacheXml.get(id);
        return element;
    }
}
