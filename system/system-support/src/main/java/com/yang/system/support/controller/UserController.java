package com.yang.system.support.controller;


import com.yang.system.client.entity.User;
import com.yang.system.support.resp.ResponseResult;
import com.yang.system.support.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("addUser")
    @ResponseBody
    public ResponseResult addUser(@RequestBody User user){
        userService.addUser(user);
        return ResponseResult.success();
    }

}
