package com.yang.system.support.service;

import com.yang.system.client.entity.MenuButton;
import com.baomidou.mybatisplus.extension.service.IService;

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

    /**
     * 更新菜单对应的按钮信息
     * */
    void updateMenuButtons(List<MenuButton> menuButtonList);

}
