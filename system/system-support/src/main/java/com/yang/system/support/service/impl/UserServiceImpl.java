package com.yang.system.support.service.impl;

import com.alibaba.nacos.common.util.Md5Utils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.system.client.entity.*;
import com.yang.system.support.constant.DrStatus;
import com.yang.system.support.constant.PermissionType;
import com.yang.system.support.dao.UserDao;
import com.yang.system.support.service.*;
import com.yang.system.support.util.IdUtils;
import com.yang.system.support.util.RandomNumAndChar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private ButtonService buttonService;

    @Autowired
    private MenuService menuService;

    @Override
    public List<Menu> getUserMenus(String userId) {
        // 1、获取用户对应的角色
        List<UserRole> userRoles = userRoleService.list(Wrappers.query(new UserRole()).eq("user_id", userId));
        if(userRoles.size()==0){
            log.info("用户没有对应的角色信息");
            return new ArrayList<>();
        }
        // 2、获取到角色对应的菜单信息
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<RolePermission> rolePermissions = rolePermissionService.list(Wrappers.query(new RolePermission()).eq("type", PermissionType.MENU).in("role_id", roleIds));
        if(rolePermissions.size()==0){
            log.info("角色无对应的菜单");
            return new ArrayList<>();
        }
        // 3、获取到菜单对应的详细信息
        List<Menu> menus = menuService.list(Wrappers.query(new Menu()).eq("dr", DrStatus.NORMAL));
        return menus;
    }

    @Override
    public List<Button> getUserButtons(String userId) {
        // 1、获取用户对应的角色
        List<UserRole> userRoles = userRoleService.list(Wrappers.query(new UserRole()).eq("user_id", userId));
        if(userRoles.size()==0){
            log.info("用户没有对应的角色信息");
            return new ArrayList<>();
        }
        // 2、获取到角色对应的按钮信息
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<RolePermission> rolePermissions = rolePermissionService.list(Wrappers.query(new RolePermission()).eq("type", PermissionType.BUTTON).in("role_id", roleIds));
        if(rolePermissions.size()==0){
            log.info("角色无对应的菜单");
            return new ArrayList<>();
        }
        // 3、获取到菜单对应的详细信息
        List<Button> buttons = buttonService.list(Wrappers.query(new Button()).eq("dr", DrStatus.NORMAL));
        return buttons;
    }

    @Override
    public void addUser(User user) {
        user.setId(IdUtils.nextId());
        user.setDr(DrStatus.NORMAL);
        String numAndChar = RandomNumAndChar.getNumAndChar(4);
        user.setSalt(numAndChar);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        // 设置密码为MD5加密
        byte[] bytes = (user.getPassword() + numAndChar).getBytes();
        String md5 = Md5Utils.getMD5(bytes);
        user.setPassword(md5);
        this.save(user);
    }
}
