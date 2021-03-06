package top.sanguohf.top.bootcon.util;

import com.alibaba.fastjson.JSONObject;
import top.sanguohf.egg.param.EntityParams;
import top.sanguohf.egg.reflect.ReflectEntity;
import top.sanguohf.egg.reflect.SubTeacher;
import top.sanguohf.egg.util.EntityConditionBuilder;
import top.sanguohf.egg.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

public class ParamEntityParseUtil {
    //把实体转换为参数
    public static <T> EntityParams parseToParam(T data) {
        EntityParams entityParams = new EntityParams();
        String name = data.getClass().getSimpleName();
        JSONObject map = null;
        try {
            map = EntityConditionBuilder.buildClass(data);
        }catch (Exception e){
            throw new RuntimeException("参数转换错误", e);
        }

        entityParams.setTableClassName(name);
        entityParams.setCondition(map);
        return entityParams;
    }

    public static void main(String[] args) throws IllegalAccessException {
        SubTeacher teacher = new SubTeacher();
        teacher.setTitle("mmmmmmm");
        teacher.setTeacherId("99990");
        teacher.setTeacherName("jjjjjj");
        EntityParams entityParams = parseToParam(teacher);
        System.out.println(entityParams);
    }

}
