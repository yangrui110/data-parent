package top.sanguohf.top.bootcon.cache;

import lombok.Data;
import top.sanguohf.egg.annotation.ViewTable;
import top.sanguohf.egg.base.EntityColumn;
import top.sanguohf.egg.reflect.ReflectEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 杨瑞
 **/
@Data
public class EntityCacheNew {

    //存放实体类的缓存信息
    private static ConcurrentHashMap<String,ClassInfo> cache = new ConcurrentHashMap();

    //加载指定的Class
    public static ClassInfo loadClass(Class entity){
        ClassInfo info = null;
        if(entity.isAnnotationPresent(ViewTable.class)){
            // 执行解析视图的方法
            info = loadViewClass(entity);
        }else {
            // 执行解析普通的实体类
            info = loadCommonClass(entity);
        }
        if(info == null) throw new RuntimeException("实体解析为空");
        return info;
    }
    private static ClassInfo loadViewClass(Class entity){

        return null;
    }
    private static ClassInfo loadCommonClass(Class entity){
        // 1、获取到类名
        String className = entity.getSimpleName();
        // 2、获取到对应的数据库表名
        String tableName = ReflectEntity.reflectTableName(entity);
        // 3、获取到待查询的列
        List<EntityColumn> columns = ReflectEntity.reflectSelectColumns(entity);
        // 4、获取到所有的属性
        List<EntityColumn> totalColumns = ReflectEntity.getTotalColumns(entity);

        EntityClassInfo classInfo = new EntityClassInfo();
        List<FieldInfo> infos = parserFieldInfoList(totalColumns);
        classInfo.setTotalFields(infos);
        List<FieldInfo> infoList = parserFieldInfoList(columns);
        classInfo.setIncludeFields(infoList);
        classInfo.setTableName(tableName);
        return classInfo;
    }

    private static FieldInfo parseToFieldInfo(EntityColumn column){
        FieldInfo info = new FieldInfo();
        info.setColumnAlias(column.getAliasColumn());
        info.setColumnName(column.getOrignColumn());
        info.setTableAlias(column.getTableAlias());
        return info;
    }

    private static List<FieldInfo> parserFieldInfoList(List<EntityColumn> columns){
        ArrayList<FieldInfo> list = new ArrayList<>();
        for(EntityColumn column: columns){
            list.add(parseToFieldInfo(column));
        }
        return list;
    }
}
