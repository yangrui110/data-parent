package com.yang.system.support.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.system.client.entity.MenuApi;
import com.yang.system.client.vo.MenuApiVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 菜单对应的接口 Mapper 接口
 * </p>
 *
 * @author yangrui
 * @since 2021-02-14
 */
public interface MenuApiDao extends BaseMapper<MenuApi> {
    Page<MenuApiVo> getMenuApis(Page page, @Param("menuApiVo") MenuApiVo menuApiVo);
}
