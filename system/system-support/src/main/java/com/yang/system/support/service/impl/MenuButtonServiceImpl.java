package com.yang.system.support.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.system.client.entity.Button;
import com.yang.system.client.entity.MenuButton;
import com.yang.system.client.entity.RolePermission;
import com.yang.system.client.po.RolePermissionSave;
import com.yang.system.client.resp.PageResult;
import com.yang.system.client.vo.MenuButtonSelect;
import com.yang.system.client.vo.MenuButtonVo;
import com.yang.system.support.constant.DrStatus;
import com.yang.system.support.constant.PermissionType;
import com.yang.system.support.dao.MenuButtonDao;
import com.yang.system.support.resp.RequestPage;
import com.yang.system.support.service.ButtonService;
import com.yang.system.support.service.MenuButtonService;
import com.yang.system.support.service.RolePermissionService;
import com.yang.system.support.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private ButtonService buttonService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    public MenuButtonSelect listButtons(Long menuId,Long roleId) {
        // 1、获取菜单对应的按钮
        MenuButtonSelect buttonSelect = new MenuButtonSelect();
        List<MenuButton> list = this.list(Wrappers.query(new MenuButton()).eq("dr", DrStatus.NORMAL).eq("menu_id", menuId));
        if(list.size()==0) return buttonSelect;
        List<Long> buttonIds = list.stream().map(MenuButton::getButtonId).collect(Collectors.toList());
        List<Button> buttons = buttonService.list(Wrappers.query(new Button()).eq("dr", DrStatus.NORMAL).in("id", buttonIds));
        buttonSelect.setMenuButtons(buttons);
        // 2、获取到角色对应的按钮
        QueryWrapper<RolePermission> queryWrapper = Wrappers.query(new RolePermission())
                .eq("dr", DrStatus.NORMAL)
                .eq("role_id", roleId)
                .eq("type", PermissionType.BUTTON)
                .in("permission_id",buttonIds);
        List<RolePermission> permissions = rolePermissionService.list(queryWrapper);
        // 3、构造按钮
        List<Button> permissionButtons = permissions.stream().map(item -> {
            Button button = new Button();
            button.setId(item.getPermissionId());
            return button;
        }).collect(Collectors.toList());
        buttonSelect.setRoleButtons(permissionButtons);
        buttonSelect.setMenuId(menuId);
        return buttonSelect;
    }

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

    @Override
    public PageResult<MenuButton> listPage(RequestPage<MenuButtonVo> menuButton) {
        // 1、加载菜单
        MenuButton data = menuButton.getData();
        if(data == null||data.getMenuId()==null) {
            return new PageResult<>();
        }
        QueryWrapper<MenuButton> query = Wrappers.query(new MenuButton());
        if(data.getId()!=null){
            query.eq("id",data.getId());
        }
        if(data.getMenuId()!=null){
            query.eq("menu_id",data.getMenuId());
        }
        if(data.getButtonId()!=null){
            query.eq("button_id",data.getButtonId());
        }
        if(data.getButtonName()!=null){
            query.like("button_name",data.getButtonName());
        }
        if(data.getButtonCode()!=null){
            query.like("button_code",data.getButtonCode());
        }
        Page<MenuButton> page = this.page(new Page<>(menuButton.getPage(), menuButton.getSize()), query);
        PageResult<MenuButton> pageResult = new PageResult<>(page.getTotal(), page.getRecords());
        return pageResult;
    }

    @Transactional
    @Override
    public void add(MenuButtonVo menuButtonVo) {
        // 1、新增button表
        Button button = new Button();
        button.setButtonCode(menuButtonVo.getButtonCode());
        button.setButtonName(menuButtonVo.getButtonName());
        button.setCreateTime(LocalDateTime.now());
        button.setUpdateTime(LocalDateTime.now());
        button.setId(IdUtils.nextId());
        button.setDr(DrStatus.NORMAL);
        buttonService.save(button);
        // 2、新增menu_button表
        MenuButton menuButton = new MenuButton();
        menuButton.setButtonCode(menuButtonVo.getButtonCode());
        menuButton.setButtonName(menuButtonVo.getButtonName());
        menuButton.setCreateTime(LocalDateTime.now());
        menuButton.setUpdateTime(LocalDateTime.now());
        menuButton.setButtonId(button.getId());
        menuButton.setId(IdUtils.nextId());
        menuButton.setDr(DrStatus.NORMAL);
        menuButton.setMenuId(menuButtonVo.getMenuId());
        this.save(menuButton);

    }

    @Transactional
    @Override
    public void update(MenuButtonVo menuButtonVo) {
        // 1、新增button表
        Button button = new Button();
        button.setButtonCode(menuButtonVo.getButtonCode());
        button.setButtonName(menuButtonVo.getButtonName());
        button.setCreateTime(LocalDateTime.now());
        button.setUpdateTime(LocalDateTime.now());
        buttonService.updateById(button);
        // 2、新增menu_button表
        MenuButton menuButton = new MenuButton();
        menuButton.setButtonCode(menuButtonVo.getButtonCode());
        menuButton.setButtonName(menuButtonVo.getButtonName());
        menuButton.setCreateTime(LocalDateTime.now());
        menuButton.setUpdateTime(LocalDateTime.now());
        menuButton.setButtonId(button.getId());
        menuButton.setMenuId(menuButtonVo.getMenuId());
        this.updateById(menuButton);
    }

    @Transactional
    @Override
    public void delete(MenuButton menuButton) {
        // 1、删除menu_button表
        this.removeById(menuButton.getId());
        // 2、删除button表
        buttonService.remove(Wrappers.query(new Button()).eq("id", menuButton.getButtonId()));
    }

    @Transactional
    @Override
    public void batchDelete(List<MenuButton> menuButtons) {
        if(menuButtons.size()==0) return;
        List<Long> menuApiIds = menuButtons.stream().map(MenuButton::getId).collect(Collectors.toList());
        this.removeByIds(menuApiIds);

        List<Long> apiIds = menuButtons.stream().map(MenuButton::getButtonId).collect(Collectors.toList());
        buttonService.removeByIds(apiIds);
    }
}
