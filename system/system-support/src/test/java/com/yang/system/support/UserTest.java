package com.yang.system.support;

import com.yang.system.client.entity.Menu;
import com.yang.system.client.vo.MenuTreeVo;
import com.yang.system.support.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @project data-parent
 * @Date 2021/2/11
 * @Auth yangrui
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserTest {

    @Autowired
    private UserService userService;

    @Test
    public void test(){
        String userId = "1359528545992224770";
        List<MenuTreeVo> userMenus = userService.getUserMenus(userId);
        System.out.println(userMenus);
    }

}
