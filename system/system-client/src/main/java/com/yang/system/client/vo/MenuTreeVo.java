package com.yang.system.client.vo;

import com.yang.system.client.entity.Menu;
import lombok.Data;

/**
 * @project data-parent
 * @Date 2021/2/12
 * @Auth yangrui
 **/
@Data
public class MenuTreeVo extends Menu {

    private Long key;

    private String title;

}
