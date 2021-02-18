package com.yang.system.support.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.system.client.entity.Role;
import com.yang.system.client.entity.RolePermission;
import com.yang.system.client.resp.PageResult;
import com.yang.system.support.constant.DrStatus;
import com.yang.system.support.dao.RoleDao;
import com.yang.system.support.resp.RequestPage;
import com.yang.system.support.service.RolePermissionService;
import com.yang.system.support.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.system.support.util.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

    @Autowired
    private RolePermissionService rolePermissionService;

    @Transactional
    @Override
    public void deleteRoleById(Role role) {
        this.removeById(role.getId());
        // 删除role_permission表的记录值
        QueryWrapper<RolePermission> queryWrapper = Wrappers.query(new RolePermission()).eq("role_id", role.getId());
        rolePermissionService.remove(queryWrapper);
    }

    @Transactional
    @Override
    public void batchDeleteRoleByIds(List<Role> roleList) {
        List<Long> roleIds = roleList.stream().map(Role::getId).collect(Collectors.toList());
        if(roleIds.size()==0){
            log.info("RoleServiceImpl.batchDeleteRoleByIds 传参的角色集合为空");
            return;
        }
        this.removeByIds(roleIds);
        // 删除role_permission表的记录值
        QueryWrapper<RolePermission> queryWrapper = Wrappers.query(new RolePermission()).in("role_id", roleIds);
        rolePermissionService.remove(queryWrapper);
    }

    @Override
    public PageResult pageList(RequestPage<Role> roleRequestPage) {
        Page<Role> page = this.page(new Page<>(roleRequestPage.getPage(), roleRequestPage.getSize()), Wrappers.query(new Role()).eq("dr", DrStatus.NORMAL));
        PageResult<Role> pageResult = new PageResult<>(page.getTotal(), page.getRecords());
        return pageResult;
    }

    @Override
    public void add(Role role) {
        role.setUpdateTime(LocalDateTime.now());
        role.setCreateTime(LocalDateTime.now());
        role.setId(IdUtils.nextId());
        role.setDr(DrStatus.NORMAL);
        save(role);
    }

    @Override
    public void update(Role role) {
        role.setUpdateTime(LocalDateTime.now());
        updateById(role);
    }
}
