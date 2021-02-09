package com.yang.system.support.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.system.client.entity.MenuButton;
import com.yang.system.support.constant.DrStatus;
import com.yang.system.support.dao.MenuButtonDao;
import com.yang.system.support.service.MenuButtonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 菜单按钮对应表 服务实现类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
@Service
public class MenuButtonServiceImpl extends ServiceImpl<MenuButtonDao, MenuButton> implements MenuButtonService {

    @Transactional
    @Override
    public void updateMenuButtons(List<MenuButton> menuButtonList) {
        Sequence sequence = new Sequence();
        // 1、删除所有的关联关系
        HashSet<Long> menuIds = new HashSet<>();
        for(MenuButton menuButton: menuButtonList){
            menuIds.add(menuButton.getMenuId());
        }
        this.remove(Wrappers.query(new MenuButton()).in("menu_id",menuIds));
        // 2、保存所有数据
        // 统一List的所有属性数目，使批量保存更高效
        ArrayList<MenuButton> menuButtons = new ArrayList<>();
        for(MenuButton menuButton: menuButtonList){
            MenuButton button = new MenuButton();
            button.setButtonId(menuButton.getButtonId());
            button.setId(sequence.nextId());
            button.setMenuId(menuButton.getMenuId());
            button.setDr(DrStatus.NORMAL);
            button.setUpdateTime(LocalDateTime.now());
            button.setCreateTime(LocalDateTime.now());
            menuButtons.add(button);
        }
        this.saveBatch(menuButtons);
    }
}
