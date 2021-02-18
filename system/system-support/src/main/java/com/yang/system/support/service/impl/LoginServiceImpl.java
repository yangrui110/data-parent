package com.yang.system.support.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.util.Md5Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yang.system.client.entity.Api;
import com.yang.system.client.entity.Role;
import com.yang.system.client.entity.User;
import com.yang.system.client.vo.UserVo;
import com.yang.system.support.constant.DrStatus;
import com.yang.system.support.exception.InterException;
import com.yang.system.support.service.LoginService;
import com.yang.system.support.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @project data-parent
 * @Date 2021/2/10
 * @Auth yangrui
 **/
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String login(String acccount, String password) {
        // 1、根据用户名，获取到指定账号信息
        QueryWrapper<User> queryWrapper = Wrappers.query(new User()).eq("dr", DrStatus.NORMAL).eq("account", acccount);
        List<User> list = userService.list(queryWrapper);
        if(list==null||list.size()==0){
            throw new InterException(500,"账号不存在");
        }
        // 2、获取到用户信息后，解析密码
        User user = list.get(0);
        String md5 = Md5Utils.getMD5((password + user.getSalt()).getBytes());
        if(!md5.equals(user.getPassword())){
            throw new InterException(500,"密码不正确");
        }
        // 3、验证成功后，则将用户Id返回出去，作为token使用
        String userId = "" + user.getId();
        // 4、用户对应的角色
        List<Role> roles = userService.getUserRoles(userId);
        // 4、获取到用户对应的api权限
        List<Api> userApis = userService.getUserApis(userId);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        userVo.setApis(userApis);
        userVo.setRoles(roles);
        stringRedisTemplate.opsForValue().set(userId, JSON.toJSONString(userVo));
        return userId;
    }

    @Override
    public void logout(Long userId) {
        stringRedisTemplate.delete(""+userId);
    }
}
