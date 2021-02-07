package top.sanguohf.egg.xml.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author 杨瑞
 **/
public class LoadFieldValue {

    // 获取到某个field的值
    public static <T> Object getValue(T data,String columnName){
        if(data instanceof Map) {
            return ((Map) data).get(columnName);
        }else {
            try {
                String s = columnName.substring(0, 1);
                String s1 = columnName.substring(1, -1);
                Method method = data.getClass().getDeclaredMethod("get" + s.toUpperCase() + s1);
                Object invoke = method.invoke(data);
                return invoke;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
