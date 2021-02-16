package com.yang.system.support.util;

import com.baomidou.mybatisplus.core.toolkit.Sequence;

/**
 * @project data-parent
 * @Date 2021/2/10
 * @Auth yangrui
 **/
public class IdUtils {


    public static long nextId(){
        long nextId = new Sequence().nextId();
        return nextId;
    }

}
