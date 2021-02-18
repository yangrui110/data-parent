package com.yang.system.client.po;

import lombok.Data;

/**
 * @project data-parent
 * @Date 2021/2/17
 * @Auth yangrui
 **/
@Data
public class RolePermissionSave {

    private Long roleId;

    private String permissionIds;

    private String lastpermissionIds;

    /**
     * 权限类型：0:menu,1:button
     * */
    private Integer type;
}
