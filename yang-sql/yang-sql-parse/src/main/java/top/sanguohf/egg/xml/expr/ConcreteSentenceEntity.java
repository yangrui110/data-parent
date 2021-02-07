package top.sanguohf.egg.xml.expr;

import lombok.Data;
import top.sanguohf.egg.reflect.ReflectEntity;
import top.sanguohf.egg.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
public class ConcreteSentenceEntity extends AbstractSentence {
    /** @pdOid 2d8a4599-01a3-45f9-bd8c-34a73cf7a404 */
    private Object key;

    private Map osData;

    public ConcreteSentenceEntity(Object key) {
        this.key = key;
    }
    public ConcreteSentenceEntity(Object key,Map osData) {
        this.key = key;
        this.osData = osData;
    }
    @Override
    public Object getResult() {
        if(!StringUtils.isNumeric((String) key)){
            // 如果是固定的字符串，不做处理
            if(((String)key).startsWith("'")&&((String)key).endsWith("'")) return ((String)key).replaceAll("'","");
            // 如果是null，也不做处理
            if("null".equalsIgnoreCase((String) key)) return null;
            String one = (String) key;
            String[] split = one.split("\\.");
            List<String> ls = new ArrayList<>();
            if(split.length==0)ls.add(one);
            else ls = Arrays.asList(split);
            Object mapData = osData;
            for(int i=0;i<ls.size();i++){
                String s = ls.get(i);
                // 如果包含[]就代表是数组
                Object o = null;
                if(mapData instanceof Map){
                     o =((Map)mapData).get(s);
                }else if(mapData instanceof List){
                    o = getListData(mapData,s);
                }else {
                    List<Field> fields = ReflectEntity.getFields(mapData.getClass());
                    for(Field field:fields){
                        if(field.getName().equalsIgnoreCase(s)){
                            field.setAccessible(true);
                            try {
                                o = field.get(mapData);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                if (i == ls.size() - 1) {
                    return o;
                }
                mapData = o;
            }
        }
        return key;
    }
    private Object getListData(Object ts,String key){
        // 截取字符串
        String left = key.substring(0, key.indexOf("["));
        String index = key.substring(key.indexOf("["), key.indexOf("]"));
        Object o = ((List) ts).get(Integer.parseInt(index));
        return o;
    }
}
