package com.yang.system.support.controller;


import com.yang.system.client.entity.Role;
import com.yang.system.client.resp.PageResult;
import com.yang.system.support.resp.RequestPage;
import com.yang.system.support.resp.ResponseResult;
import com.yang.system.support.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 用户列表
     * */
    @ResponseBody
    @PostMapping("pageList")
    public ResponseResult pageList(@RequestBody RequestPage<Role> menuButton){
        PageResult<Role> menuButtonPage = roleService.pageList(menuButton);
        return ResponseResult.success(menuButtonPage);
    }

    @ResponseBody
    @PostMapping("add")
    public ResponseResult add(@RequestBody Role role){
        roleService.add(role);
        return ResponseResult.success();
    }
    @ResponseBody
    @PostMapping("update")
    public ResponseResult update(@RequestBody Role role){
        roleService.update(role);
        return ResponseResult.success();
    }
}
