package com.yang.system.client.vo;

import com.yang.system.client.entity.MenuApi;
import lombok.Data;

/**
 * @project data-parent
 * @Date 2021/2/14
 * @Auth yangrui
 **/
@Data
public class MenuApiVo extends MenuApi {

    /**
     * api路径
     * */
    private String apiPath;
}
