package com.yang.system.support.util;

import com.alibaba.fastjson.JSONObject;
import com.yang.system.client.entity.User;
import com.yang.system.client.vo.UserVo;
import com.yang.system.support.exception.InterException;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * @project data-parent
 * @Date 2021/2/17
 * @Auth yangrui
 **/
public class UserInfoUtil {

    public static Long getUserId(HttpServletRequest request){
        String userId = request.getHeader("Access-Token");
        if(userId==null) throw new InterException(40006,"用户未登录");
        return Long.parseLong(userId);
    }

    public static UserVo getUserInfo(HttpServletRequest request, StringRedisTemplate redisTemplate){
        String userId = request.getHeader("Access-Token");
        if(userId==null) throw new InterException(40006,"用户未登录");
        String s = redisTemplate.opsForValue().get(userId);
        UserVo userVo = JSONObject.parseObject(s).toJavaObject(UserVo.class);
        return userVo;
    }

}
