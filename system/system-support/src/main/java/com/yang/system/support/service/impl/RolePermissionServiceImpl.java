package com.yang.system.support.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yang.system.client.entity.RolePermission;
import com.yang.system.client.po.RolePermissionSave;
import com.yang.system.support.constant.DrStatus;
import com.yang.system.support.constant.PermissionType;
import com.yang.system.support.dao.RolePermissionDao;
import com.yang.system.support.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.system.support.util.IdUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单权限对应表：1、对应Menu表2、对应button表 服务实现类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionDao, RolePermission> implements RolePermissionService {

    @Override
    public List<RolePermission> listMenuByRoleId(Long roleId) {
        List<RolePermission> list = this.list(Wrappers.query(new RolePermission()).eq("dr", DrStatus.NORMAL).eq("type", PermissionType.MENU).eq("role_id", roleId));

        return list;
    }

    @Transactional
    @Override
    public void save(RolePermissionSave rolePermissionSave) {
        // 1、删除旧的(应该是把旧的全部删除)
        this.remove(Wrappers.query(new RolePermission()).eq("role_id",rolePermissionSave.getRoleId()).eq("type",rolePermissionSave.getType()));
        // 2、插入新的
        String permissionIds = rolePermissionSave.getPermissionIds();
        if(!StringUtils.isBlank(permissionIds)){
            ArrayList<RolePermission> rolePermissions = new ArrayList<>();
            String[] split = permissionIds.split(",");
            for(String one: split){
                RolePermission permission = new RolePermission();
                permission.setId(IdUtils.nextId());
                permission.setCreateTime(LocalDateTime.now());
                permission.setPermissionId(Long.parseLong(one));
                permission.setDr(DrStatus.NORMAL);
                permission.setUpdateTime(LocalDateTime.now());
                permission.setType(rolePermissionSave.getType());
                permission.setRoleId(rolePermissionSave.getRoleId());
                rolePermissions.add(permission);
            }
            this.saveBatch(rolePermissions);
        }
    }
}
