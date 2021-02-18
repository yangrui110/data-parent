package com.yang.system.support.service;

import com.yang.system.client.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.system.client.resp.PageResult;
import com.yang.system.support.resp.RequestPage;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
public interface RoleService extends IService<Role> {

    /**
     * 删除一个角色，同时也要删除和权限相关的记录
     * */
    void deleteRoleById(Role role);

    /**
     * 删除多个角色
     * */
    void batchDeleteRoleByIds(List<Role> roleList);
    /**
     * 分页获取Role
     * */
    PageResult pageList(RequestPage<Role> menuButton);
    void add(Role role);
    void update(Role role);
}
