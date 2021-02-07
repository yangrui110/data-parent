package top.sanguohf.egg.util;

import top.sanguohf.egg.enums.InitSqlType;
import top.sanguohf.egg.ops.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ConsoleSqlUtil {

    /**
     * 打印SQL语句
     * */
    public static void console(String sql, InitSqlType sqlType){
        String format = "===> %s：%s";
        String type = "";
        if(sqlType.getType()==InitSqlType.UPDATE.getType()){
            type = "UPDATE";
        }else if(sqlType.getType()==InitSqlType.INSERT.getType()){
            type = "INSERT";
        }else if(sqlType.getType()==InitSqlType.DELETE.getType()){
            type = "DELETE";
        }else if(sqlType.getType()==InitSqlType.SELECT.getType()){
            type = "SELECT";
        }else type ="预编译SQL";

        String s = String.format(format,type, sql);
        System.out.println(s);
    }

    /**
     * 打印SQL语句
     * */
    public static void console(String sql, EntityJoinTable joinTable){
        String format = "===> %s：%s";
        String type = "";
        if(joinTable instanceof EntityUpdateSql){
            type = "UPDATE";
        }else if(joinTable instanceof EntityInsertSql){
            type = "INSERT";
        }else if(joinTable instanceof EntityDeleteSql){
            type = "DELETE";
        }else if(joinTable instanceof EntitySelectSql){
            type = "SELECT";
        }else type ="预编译SQL";

        String s = String.format(format,type, sql);
        System.out.println(s);
    }
    /**
     * 打印数据库列
     * */
    public static void consoleColumns(List<String> columns){
        String format = "<=== 数据库列: %s";
        StringBuilder builder = new StringBuilder();
        for(String column: columns){
            builder.append(column).append(",");
        }
        if(builder.length()>0) format = String.format(format,builder.substring(0,builder.length()-1));
        System.out.println(format);
    }

    /**
     * 打印参数详情
     * */
    public static void consoleParam(List<Object> params){
        System.out.print("参数：");
        consoleParam(params,0);
    }
    /**
     * 打印参数详情
     * */
    public static void consoleParam(List<Object> params,int offset){
        StringBuilder builder = new StringBuilder();
        int i =0;
        int size = params.size();
        for(Object os : params){
            if(os instanceof String|os instanceof Integer|os instanceof Long|os instanceof Byte|os instanceof Double|os instanceof Character|os instanceof CharSequence
                    |os instanceof Short|os instanceof Boolean|os instanceof Integer){
                builder.append(os).append(" ");
                if(i!=size-1)
                    builder.append(",");
            }else if(os instanceof BigDecimal){
                builder.append(((BigDecimal) os).toPlainString()).append(" ");
                if(i!=size-1)
                    builder.append(",");
            }else if(os instanceof List) {
                builder.append("\n");
                builder.append("===>Data").append(i+offset).append(": ");
                for(Object one : (List)os){
                    if(one instanceof Map) {
                        buildMap((Map) one,builder);
                    }else {
                        buildOne(one,builder);
                    }
                }
            }
            i++;
        }
        System.out.println(builder.toString());
    }

    /**
     * 打印结果集
     * */
    public static void consoleResult(Object os){
        StringBuilder builder = new StringBuilder();
        if(os instanceof List){
            buildSelectResultList((List) os,builder);
        }else if(os instanceof Map){
            buildMap((Map) os,builder);
        }else {builder.append(os);}
        System.out.println(builder.toString());
    }

    /**
     * 构建查询结果集
     * */
    private static void buildSelectResultList(List os,StringBuilder builder){
        int count = 1;
        for(Object one : os){
            builder.append("===>Row").append(count).append(": ");
            if(one instanceof Map) {
                buildMap((Map) one,builder);
            }else {
                buildOne(one,builder);
            }
            builder.append("\n");
            count++;
        }
    }
    /**
     * 打印Map的Key和Value
     * */
    private static void buildMap(Map one,StringBuilder builder){
        for(Object key: one.keySet()){
            Object os = one.get(key);
            buildOne(os,builder);
        }
    }
    /**
     * 打印单个值
     * */
    private static void buildOne(Object os,StringBuilder builder){
        if(os instanceof BigDecimal){
            builder.append(((BigDecimal) os).toPlainString()).append(" ");
        }else if(os instanceof String | os instanceof Integer | os instanceof Long | os instanceof Byte | os instanceof Double | os instanceof Character | os instanceof CharSequence
                | os instanceof Short | os instanceof Boolean | os instanceof Integer) {
            builder.append(os).append(" ");
        }
    }

}
