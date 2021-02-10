package com.yang.system.support.controller;


import com.yang.system.support.resp.ResponseResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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


    @GetMapping("test")
    @ResponseBody
    public ResponseResult test(){
        return ResponseResult.success();
    }

}
