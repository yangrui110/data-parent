package com.yang.system.support.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.system.client.entity.MenuApi;
import com.yang.system.client.resp.PageResult;
import com.yang.system.client.vo.MenuApiSelect;
import com.yang.system.client.vo.MenuApiVo;
import com.yang.system.support.resp.RequestPage;

import java.util.List;

/**
 * <p>
 * 菜单对应的接口 服务类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-14
 */
public interface MenuApiService extends IService<MenuApi> {

    /**
     * 获取MenuApi的结果
     * */
    PageResult<MenuApiVo> getMenuApis(RequestPage<MenuApiVo> menuApiVo);
    /**
     * 新增一条Api记录，以及menu_api记录
     * */
    void add(MenuApiVo menuApiVo);
    /**
     * 更新一条Api记录
     * */
    void update(MenuApiVo menuApiVo);
    /**
     * 删除一条menu_api记录和api记录
     * */
    void delete(MenuApi menuApi);
    /**
     * 批量删除menu_api记录和api记录
     * */
    void batchDelete(List<MenuApi> menuApis);
    /**
     * 根据角色ID和菜单ID获取到对应的结果
     * */
    MenuApiSelect listApiByRoleIdAndMenuId(Long menuId, Long roleId);
}
