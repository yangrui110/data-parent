package com.yang.system.support.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.sanguohf.egg.param.EntityParams;
import top.sanguohf.top.bootcon.service.CommonService;

import java.util.List;

/**
 * @project data-parent
 * @Date 2021/2/7
 * @Auth yangrui
 **/
@Controller
@RequestMapping("common")
public class TestController {

    @Autowired
    CommonService commonService;

    @ResponseBody
    @PostMapping("/findPageList")
    public List findPageList(@RequestBody EntityParams entityParams){
        List entityList = commonService.findList(entityParams);
        System.out.println(entityList);
        return entityList;
    }



}
