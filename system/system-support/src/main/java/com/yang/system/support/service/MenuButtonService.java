package com.yang.system.support.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.system.client.entity.Button;
import com.yang.system.client.entity.MenuButton;
import com.yang.system.client.resp.PageResult;
import com.yang.system.client.vo.MenuButtonSelect;
import com.yang.system.client.vo.MenuButtonVo;
import com.yang.system.support.resp.RequestPage;

import java.util.List;

/**
 * <p>
 * 菜单按钮对应表 服务类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
public interface MenuButtonService extends IService<MenuButton> {

    MenuButtonSelect listButtons(Long menuId,Long roleId);

    /**
     * 更新菜单对应的按钮信息
     * */
    void updateMenuButtons(List<MenuButton> menuButtonList);

    /**
     * 分页查询菜单对应的按钮信息
     * */
    PageResult<MenuButton> listPage(RequestPage<MenuButtonVo> menuButton);

    /**
     * 新增一条button记录和menu_button记录
     * */
    void add(MenuButtonVo menuButtonVo);

    /**
     * 更新一条button记录和menu_button记录
     * */
    void update(MenuButtonVo menuButtonVo);

    /**
     * 删除一条记录，同时删除按钮表
     * */
    void delete(MenuButton menuButton);
    /**
     * 批量删除menu_api记录和api记录
     * */
    void batchDelete(List<MenuButton> menuApis);

}
