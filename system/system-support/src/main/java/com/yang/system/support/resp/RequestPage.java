package com.yang.system.support.resp;

import lombok.Data;

/**
 * @project data-parent
 * @Date 2021/2/7
 * @Auth yangrui
 **/
@Data
public class RequestPage<T> {
    private T data;

    private int page;

    private int size;

}
