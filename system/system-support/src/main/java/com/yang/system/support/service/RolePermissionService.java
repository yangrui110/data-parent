package com.yang.system.support.service;

import com.yang.system.client.entity.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.system.client.po.RolePermissionSave;

import java.util.List;

/**
 * <p>
 * 菜单权限对应表：1、对应Menu表2、对应button表 服务类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
public interface RolePermissionService extends IService<RolePermission> {

    List<RolePermission> listMenuByRoleId(Long roleId);

    void save(RolePermissionSave rolePermissionSave);

}
