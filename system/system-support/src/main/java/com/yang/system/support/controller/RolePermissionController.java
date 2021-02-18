package com.yang.system.support.controller;


import com.yang.system.client.entity.RolePermission;
import com.yang.system.client.po.RolePermissionSave;
import com.yang.system.support.resp.ResponseResult;
import com.yang.system.support.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单权限对应表：1、对应Menu表2、对应button表 前端控制器
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
@Controller
@RequestMapping("/rolePermission")
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;
    /**
     * 根据角色ID获取到菜单
     * */
    @ResponseBody
    @GetMapping("listMenuByRoleId")
    public ResponseResult roleMenus(@RequestParam Long roleId){
        List<RolePermission> rolePermissions = rolePermissionService.listMenuByRoleId(roleId);
        return ResponseResult.success(rolePermissions);
    }

    @ResponseBody
    @PostMapping("save")
    public ResponseResult save(@RequestBody RolePermissionSave rolePermissionSave){
        rolePermissionService.save(rolePermissionSave);
        return ResponseResult.success();
    }
}
