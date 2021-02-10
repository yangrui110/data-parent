package com.yang.system.support.controller;

import com.yang.system.client.entity.User;
import com.yang.system.support.resp.ResponseResult;
import com.yang.system.support.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @project data-parent
 * @Date 2021/2/10
 * @Auth yangrui
 **/
@Controller
public class LoginController {


    @Autowired
    private LoginService loginService;
    /**
     * 登录后台系统
     * */
    @ResponseBody
    @PostMapping("login")
    public ResponseResult login(@RequestBody User user){
        String token = loginService.login(user.getAccount(), user.getPassword());
        return ResponseResult.success(token);
    }

}
