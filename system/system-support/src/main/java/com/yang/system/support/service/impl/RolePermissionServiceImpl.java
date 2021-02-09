package com.yang.system.support.service.impl;

import com.yang.system.client.entity.RolePermission;
import com.yang.system.support.dao.RolePermissionDao;
import com.yang.system.support.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
