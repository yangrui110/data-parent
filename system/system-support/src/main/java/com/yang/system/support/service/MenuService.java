package com.yang.system.support.service;

import com.yang.system.client.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
public interface MenuService extends IService<Menu> {

    /**
     * 删除一个菜单，同时也要删除菜单对应的按钮关联
     * */
    void deleteMenuById(Menu menu);

}
