package com.yang.system.support.controller;

import com.yang.system.client.entity.User;
import com.yang.system.support.resp.ResponseResult;
import com.yang.system.support.service.LoginService;
import com.yang.system.support.util.UserInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @project data-parent
 * @Date 2021/2/10
 * @Auth yangrui
 **/
@Controller
public class LoginController {


    @Autowired
    private LoginService loginService;
    @Autowired
    private HttpServletRequest request;
    /**
     * 登录后台系统
     * */
    @ResponseBody
    @PostMapping("login")
    public ResponseResult login(@RequestBody User user){
        String token = loginService.login(user.getAccount(), user.getPassword());
        return ResponseResult.success(token);
    }

    @ResponseBody
    @PostMapping("logout")
    public ResponseResult logout(){
        Long userId = UserInfoUtil.getUserId(request);
        loginService.logout(userId);
        return ResponseResult.success();
    }

}
