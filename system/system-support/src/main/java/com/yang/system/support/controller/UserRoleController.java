package com.yang.system.support.controller;


import com.yang.system.client.po.UserRoleBatchMap;
import com.yang.system.support.resp.ResponseResult;
import com.yang.system.support.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 用户角色对应表 前端控制器
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
@Controller
@RequestMapping("/userRole")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @ResponseBody
    @PostMapping("updateUserRole")
    public ResponseResult updateUserRole(@RequestBody UserRoleBatchMap userRoleBatchMap){
        userRoleService.updateUserRole(userRoleBatchMap);
        return ResponseResult.success();
    }

}
