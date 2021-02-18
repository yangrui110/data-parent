package com.yang.system.support.controller;


import com.alibaba.fastjson.JSONObject;
import com.yang.system.client.entity.Api;
import com.yang.system.client.entity.Role;
import com.yang.system.client.entity.User;
import com.yang.system.client.po.ModifyPassword;
import com.yang.system.client.resp.PageResult;
import com.yang.system.client.vo.MenuTreeVo;
import com.yang.system.support.constant.DrStatus;
import com.yang.system.support.exception.InterException;
import com.yang.system.support.resp.RequestPage;
import com.yang.system.support.resp.ResponseResult;
import com.yang.system.support.service.UserService;
import com.yang.system.support.util.UserInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.sanguohf.egg.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
    private HttpServletRequest request;

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取到该用户可操作的菜单
     * */
    @ResponseBody
    @GetMapping("getUserMenus")
    public ResponseResult getUserMenus(){
        Long userId = UserInfoUtil.getUserId(request);
        List<MenuTreeVo> userMenus = userService.getUserMenus(""+userId);
        return ResponseResult.success(userMenus);
    }

    @ResponseBody
    @GetMapping("getUserRoles")
    public ResponseResult getUserRoles(@RequestParam String userId){
        List<Role> userMenus = userService.getUserRoles(userId);
        return ResponseResult.success(userMenus);
    }

    @PostMapping("addUser")
    @ResponseBody
    public ResponseResult addUser(@RequestBody User user){
        userService.addUser(user);
        return ResponseResult.success();
    }

    @PostMapping("update")
    @ResponseBody
    public ResponseResult update(@RequestBody User user){
        userService.updateById(user);
        return ResponseResult.success();
    }

    @GetMapping("queryById")
    @ResponseBody
    public ResponseResult queryById(Long userId){
        User byId = userService.getById(userId);
        return ResponseResult.success(byId);
    }


    /**
     * 根据token获取到用户信息
     * */
    @GetMapping("info")
    @ResponseBody
    public ResponseResult getUserInfoByToken(){
        String accessToken = request.getHeader("Access-Token");
        String s = stringRedisTemplate.opsForValue().get(accessToken);
        if(!StringUtils.isEmpty(s)){
            User user = JSONObject.parseObject(s).toJavaObject(User.class);
            return ResponseResult.success(user);
        }
        return ResponseResult.error(408,"用户信息不存在，请重新登录");
    }

    /**
     * 用户列表
     * */
    @ResponseBody
    @PostMapping("pageList")
    public ResponseResult pageList(@RequestBody RequestPage<User> menuButton){
        PageResult<User> menuButtonPage = userService.pageList(menuButton);
        return ResponseResult.success(menuButtonPage);
    }

    @ResponseBody
    @DeleteMapping("batchDelete")
    public ResponseResult batchDelete(String ids){
        if(StringUtils.isEmpty(ids)) return ResponseResult.error();
        String[] split = ids.split(",");
        ArrayList<User> list = new ArrayList<>();
        for(String one: split){
            User menu = new User();
            menu.setId(Long.parseLong(one));
            menu.setDr(DrStatus.DEL);
            list.add(menu);
        }
        userService.updateBatchById(list);
        return ResponseResult.success();
    }

    @ResponseBody
    @DeleteMapping("delete")
    public ResponseResult delete(String id){
        if(StringUtils.isEmpty(id)) return ResponseResult.error();
        User menu = new User();
        menu.setId(Long.parseLong(id));
        menu.setDr(DrStatus.DEL);
        userService.updateById(menu);
        return ResponseResult.success();
    }

    @ResponseBody
    @PostMapping("modifyPassword")
    public ResponseResult modifyPassword(@RequestBody ModifyPassword modifyPassword){
        Long userId = UserInfoUtil.getUserId(request);
        modifyPassword.setUserId(userId);
        userService.modifyPassword(modifyPassword);
        return ResponseResult.success();
    }

}
