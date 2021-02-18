package com.yang.system.support.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yang.system.client.entity.UserRole;
import com.yang.system.client.po.UserRoleBatchMap;
import com.yang.system.support.constant.DrStatus;
import com.yang.system.support.dao.UserRoleDao;
import com.yang.system.support.exception.InterException;
import com.yang.system.support.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.system.support.util.IdUtils;
import org.springframework.stereotype.Service;
import top.sanguohf.egg.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * <p>
 * 用户角色对应表 服务实现类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRole> implements UserRoleService {

    @Override
    public void updateUserRole(UserRoleBatchMap userRoleBatchMap) {
        // 1、检测数据是否为空
        if(StringUtils.isEmpty(userRoleBatchMap.getRoleIds())){
            throw new InterException(5000,"角色ID不能为空");
        }
        if(userRoleBatchMap.getUserId()==null){
            throw new InterException(4000,"用户ID不能为空");
        }
        // 2、删除旧数据
        this.remove(Wrappers.query(new UserRole()).eq("user_id",userRoleBatchMap.getUserId()));
        // 3、构造新数据
        String[] split = userRoleBatchMap.getRoleIds().split(",");
        ArrayList<UserRole> userRoles = new ArrayList<>();
        for(String one: split){
            UserRole userRole = new UserRole();
            userRole.setId(IdUtils.nextId());
            userRole.setCreateTime(LocalDateTime.now());
            userRole.setDr(DrStatus.NORMAL);
            userRole.setUpdateTime(LocalDateTime.now());
            userRole.setRoleId(Long.parseLong(one));
            userRole.setUserId(userRoleBatchMap.getUserId());
            userRoles.add(userRole);
        }
        // 4、保存
        this.saveBatch(userRoles);
    }
}
