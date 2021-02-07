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
        return entityList;
    }

    @ResponseBody
    @PostMapping("insert")
    public void insert(@RequestBody EntityParams entityParams){
        commonService.insert(entityParams);
    }

    @ResponseBody
    @PostMapping("batchInsert")
    public void batchInsert(@RequestBody List<EntityParams> entityParams){
        commonService.batchInsert(entityParams);
    }

    @ResponseBody
    @PostMapping("update")
    public void update(@RequestBody EntityParams entityParams){
        commonService.update(entityParams);
    }

    @ResponseBody
    @PostMapping("batchUpdate")
    public void update(@RequestBody List<EntityParams> entityParams){
        commonService.batchUpdate(entityParams);
    }

    @ResponseBody
    @PostMapping("delete")
    public void delete(@RequestBody EntityParams entityParams){
        commonService.delete(entityParams);
    }

    @ResponseBody
    @PostMapping("batchDelete")
    public void batchDelete(@RequestBody List<EntityParams> entityParams){
        commonService.batchDelete(entityParams);
    }

}
