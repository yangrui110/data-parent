package com.yang.system.client.vo;

import com.yang.system.client.entity.Api;
import lombok.Data;

import java.util.List;

/**
 * @project data-parent
 * @Date 2021/2/17
 * @Auth yangrui
 **/
@Data
public class MenuApiSelect {

    /**
     * 菜单对应的api信息
     * */
    private List<Api> menuApis;

    private Long menuId;

    /**
     * 角色对应的api信息
     * */
    private List<Api> roleApis;

}
