package com.yang.system.support.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yang.system.client.entity.Menu;
import com.yang.system.client.vo.MenuTreeVo;
import com.yang.system.support.constant.DrStatus;
import com.yang.system.support.resp.ResponseResult;
import com.yang.system.support.service.MenuService;
import com.yang.system.support.util.IdUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import top.sanguohf.egg.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 获取到所有的菜单列表
     * */
    @ResponseBody
    @GetMapping("list")
    public ResponseResult list(){
        List<Menu> list = menuService.list(Wrappers.query(new Menu()).eq("dr",DrStatus.NORMAL));
        ArrayList<MenuTreeVo> arrayList = new ArrayList<>();
        for(Menu menu: list){
            MenuTreeVo vo = new MenuTreeVo();
            BeanUtils.copyProperties(menu,vo);
            vo.setKey(menu.getId());
            vo.setTitle(menu.getMenuName());
            arrayList.add(vo);
        }
        return ResponseResult.success(arrayList);
    }

    @ResponseBody
    @PostMapping("update")
    public ResponseResult update(@RequestBody Menu menu){
        menuService.updateById(menu);
        return ResponseResult.success();
    }

    @ResponseBody
    @PostMapping("add")
    public ResponseResult add(@RequestBody Menu menu){
        menu.setId(IdUtils.nextId());
        menu.setDr(DrStatus.NORMAL);
        menuService.save(menu);
        return ResponseResult.success();
    }

    @ResponseBody
    @DeleteMapping("batchDelete")
    public ResponseResult batchDelete(String ids){
        if(StringUtils.isEmpty(ids)) return ResponseResult.error();
        String[] split = ids.split(",");
        ArrayList<Menu> list = new ArrayList<>();
        for(String one: split){
            Menu menu = new Menu();
            menu.setId(Long.parseLong(one));
            menu.setDr(DrStatus.DEL);
            list.add(menu);
        }
        menuService.updateBatchById(list);
        return ResponseResult.success();
    }

    @ResponseBody
    @DeleteMapping("delete")
    public ResponseResult delete(String id){
        if(StringUtils.isEmpty(id)) return ResponseResult.error();
        Menu menu = new Menu();
        menu.setId(Long.parseLong(id));
        menu.setDr(DrStatus.DEL);
        menuService.updateById(menu);
        return ResponseResult.success();
    }

}
