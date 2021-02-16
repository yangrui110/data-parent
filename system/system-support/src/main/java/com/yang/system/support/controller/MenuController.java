package com.yang.system.support.controller;


import com.yang.system.client.entity.Menu;
import com.yang.system.support.constant.DrStatus;
import com.yang.system.support.resp.ResponseResult;
import com.yang.system.support.service.MenuService;
import com.yang.system.support.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import top.sanguohf.egg.util.StringUtils;

import java.util.ArrayList;

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
