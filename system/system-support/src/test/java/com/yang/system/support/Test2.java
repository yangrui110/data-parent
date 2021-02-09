package com.yang.system.support;

import com.baomidou.mybatisplus.core.toolkit.Sequence;

/**
 * @project data-parent
 * @Date 2021/2/9
 * @Auth yangrui
 **/
public class Test2 {
    public static void main(String[] args) {
        Sequence generator = new Sequence();
        for(int i=0;i<20;i++){
            long nextId = generator.nextId();
            System.out.println(nextId);
        }
    }
}
