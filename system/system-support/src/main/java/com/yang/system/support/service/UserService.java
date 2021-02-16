package com.yang.system.support.service;

import com.yang.system.client.entity.Button;
import com.yang.system.client.entity.Menu;
import com.yang.system.client.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
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
}
