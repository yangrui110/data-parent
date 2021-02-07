package top.sanguohf.egg.xml.variable;

import java.util.ArrayList;
import java.util.List;

/**
 * @project platform
 * @Date 2020/9/1
 * @Auth 杨瑞
 **/
public class VariableAdd {

    public static List<ParentVariable> addValue(List<ParentVariable> variables,String key,Object data){
        ArrayList<ParentVariable> list = new ArrayList<>();
        list.addAll(variables);
        if(data!=null) {
            ParentVariable variable = new ParentVariable();
            variable.setKey(key);
            variable.setData(data);
            list.add(variable);
        }
        return list;
    }
}
