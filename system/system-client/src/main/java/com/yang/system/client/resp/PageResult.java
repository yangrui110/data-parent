package com.yang.system.client.resp;

import lombok.Data;

import java.util.List;

/**
 * @project data-parent
 * @Date 2021/2/14
 * @Auth yangrui
 **/
@Data
public class PageResult<T> {
    private int totalNum;
    private List<T> data;
    private String tips;

    public PageResult() {
        this.totalNum = 0;
    }

    public PageResult(int totalNum, List<T> data) {
        this.totalNum = totalNum;
        this.data = data;
    }
    public PageResult(int totalNum, List<T> data,String tips){
        this(totalNum,data);
        this.tips=tips;
    }

    public PageResult(Long totalNum, List<T> data) {
        if(totalNum!=null) {
            this.totalNum = Integer.parseInt(Long.toString(totalNum));
        }
        this.data = data;
    }
    public PageResult(Long totalNum, List<T> data,String tips){
        this(totalNum,data);
        this.tips=tips;
    }
}
