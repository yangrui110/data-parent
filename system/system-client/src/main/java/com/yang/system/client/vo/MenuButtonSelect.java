package com.yang.system.client.vo;

import com.yang.system.client.entity.Button;
import lombok.Data;

import java.util.List;

/**
 * @project data-parent
 * @Date 2021/2/17
 * @Auth yangrui
 **/
@Data
public class MenuButtonSelect {

    /**
     * 菜单对应的按钮信息
     * */
    private List<Button> menuButtons;

    private Long menuId;

    /**
     * 角色对应的按钮信息
     * */
    private List<Button> roleButtons;

}
