package top.sanguohf.egg.util;

import com.alibaba.fastjson.JSONObject;
import top.sanguohf.egg.reflect.ReflectEntity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @project platform
 * @Date 2020/9/6
 * @Auth 杨瑞
 **/
public class UtilMap {

    public static Map toMap(Object os) {
        String jsonString = JSONObject.toJSONString(os);
        JSONObject object = JSONObject.parseObject(jsonString);
        return object;
        /*List<Field> fields = ReflectEntity.getFields(os.getClass());
        HashMap<Object, Object> map = new HashMap<>();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object o = field.get(os);
                map.put(field.getName(),o);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }*/
//        return map;
    }
}
