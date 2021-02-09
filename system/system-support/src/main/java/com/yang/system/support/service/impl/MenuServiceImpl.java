package com.yang.system.support.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yang.system.client.entity.Menu;
import com.yang.system.client.entity.MenuButton;
import com.yang.system.support.constant.DrStatus;
import com.yang.system.support.dao.MenuDao;
import com.yang.system.support.service.MenuButtonService;
import com.yang.system.support.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuDao, Menu> implements MenuService {

    @Autowired
    private MenuButtonService menuButtonService;

    @Transactional
    @Override
    public void deleteMenuById(Menu menu) {
        // 逻辑删除菜单
        Menu updateMenu = new Menu();
        updateMenu.setId(menu.getId());
        updateMenu.setDr(DrStatus.DEL);
        this.updateById(updateMenu);
        // 移除菜单按钮的对应关系
        menuButtonService.remove(Wrappers.query(new MenuButton()).eq("menu_id", menu.getId()));
    }
}
