package com.yang.system.support.service;

import com.yang.system.client.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.system.client.po.ModifyPassword;
import com.yang.system.client.resp.PageResult;
import com.yang.system.client.vo.MenuTreeVo;
import com.yang.system.support.resp.RequestPage;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
public interface UserService extends IService<User> {

    /**
     * 获取用户可操作的菜单
     * */
    List<MenuTreeVo> getUserMenus(String userId);

    List<Role> getUserRoles(String userId);
    /**
     * 返回用户对应的APi信息
     * */
    List<Api> getUserApis(String userId);

    List<Api> getUserApisByRedis();
    /**
     * 获取用户的按钮权限
     * */
    List<Button> getUserButtons(String userId);

    /**
     * 新增一个用户
     * */
    void addUser(User user);

    /**
     * 获取用户列表
     * */
    PageResult<User> pageList(RequestPage<User> requestPage);
    /**
     * 修改密码
     * */
    void modifyPassword(ModifyPassword modifyPassword);
}
