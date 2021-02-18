package com.yang.system.support.service;

import com.yang.system.client.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.system.client.po.UserRoleBatchMap;

/**
 * <p>
 * 用户角色对应表 服务类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
public interface UserRoleService extends IService<UserRole> {

    void updateUserRole(UserRoleBatchMap userRoleBatchMap);

}
