package top.sanguohf.top.bootcon.util;

import top.sanguohf.egg.ops.EntityJoinTable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 杨瑞
 **/
public class BatchSupport {

    /**
     * 是否支持批量操作
     * */
    public static boolean isSuppport(List<? extends EntityJoinTable> joinTables){
        // 1、获取到每个实体的参数数量
        ArrayList list = new ArrayList<>();
        int count  = 0;
        for(int i =0;i<joinTables.size();i++){
            EntityJoinTable joinTable = joinTables.get(i);
            joinTable.addValue(list);
            if(i == 0){
                count = list.size();
            }else {
                if(count != list.size()) return false;
            }
            list = new ArrayList();
        }
        return true;
    }

}
