package com.yang.system.support.controller;


import com.yang.system.client.entity.MenuButton;
import com.yang.system.client.resp.PageResult;
import com.yang.system.client.vo.MenuButtonSelect;
import com.yang.system.client.vo.MenuButtonVo;
import com.yang.system.support.resp.RequestPage;
import com.yang.system.support.resp.ResponseResult;
import com.yang.system.support.service.MenuButtonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单按钮对应表 前端控制器
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
@Controller
@RequestMapping("/menuButton")
public class MenuButtonController {

    @Autowired
    private MenuButtonService menuButtonService;

    /**
     * 根据menuId获取到Button信息
     * */
    @ResponseBody
    @GetMapping("listButtons")
    public ResponseResult listButtons(@RequestParam Long menuId,@RequestParam Long roleId){
        MenuButtonSelect menuButtonSelect = menuButtonService.listButtons(menuId, roleId);
        return ResponseResult.success(menuButtonSelect);
    }
    @ResponseBody
    @PostMapping("pageList")
    public ResponseResult pageList(@RequestBody RequestPage<MenuButtonVo> menuButton){
        PageResult<MenuButton> menuButtonPage = menuButtonService.listPage(menuButton);
        return ResponseResult.success(menuButtonPage);
    }

    @ResponseBody
    @PostMapping("add")
    public ResponseResult add(@RequestBody MenuButtonVo menuButtonVo){
        menuButtonService.add(menuButtonVo);
        return ResponseResult.success();
    }

    @ResponseBody
    @PostMapping("delete")
    public ResponseResult delete(@RequestBody MenuButton menuButton){
        menuButtonService.delete(menuButton);
        return ResponseResult.success();
    }

    @ResponseBody
    @PostMapping("update")
    public ResponseResult update(@RequestBody MenuButtonVo menuApiVo){
        menuButtonService.update(menuApiVo);
        return ResponseResult.success();
    }

    @ResponseBody
    @PostMapping("batchDelete")
    public ResponseResult batchDelete(@RequestBody List<MenuButton> menuApis){
        menuButtonService.batchDelete(menuApis);
        return ResponseResult.success();
    }

}
